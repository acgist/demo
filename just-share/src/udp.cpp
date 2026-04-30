#include "jshare/udp.hpp"

#include <mutex>
#include <chrono>
#include <cstring>
#include <fstream>
#include <iostream>

#ifdef _WIN32
#pragma comment(lib, "Ws2_32.lib")
#endif

#define BROADCAST_PORT 18888
#define BROADCAST_HOST "127.0.0.1"

static std::mutex mutex;
static std::condition_variable cond;

bool jshare::socket::init() {
    #ifdef _WIN32
	WSADATA lpWSAData;
	WORD wVersionRequested = MAKEWORD(2, 2);
	WSAStartup(wVersionRequested, &lpWSAData);
    #endif
    return true;
}

jshare::Udp::Udp(std::function<void(const int, const char*, const int, const sockaddr_in&)> callback) : callback(callback) {
    this->init();
    this->recv();
}

jshare::Udp::~Udp() {
    this->running = false;
    if(this->recv_thread) {
        this->recv_thread->join();
    }
    if(this->bcst_thread) {
        this->bcst_thread->join();
    }
    #if _WIN32
    closesocket(this->bcst_socket);
    #else
    close(this->bcst_socket);
    #endif
}

void jshare::Udp::init() {
    if(this->running) {
        return;
    }
    this->running = true;
    this->recv_data.resize(this->vector_size);
    this->send_data.resize(this->vector_size);
    this->bcst_socket = ::socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    // 监听地址
    this->bcst_recv_addr.sin_family      = AF_INET;
    this->bcst_recv_addr.sin_port        = htons(BROADCAST_PORT);
    this->bcst_recv_addr.sin_addr.s_addr = htonl(INADDR_ANY); // inet_addr("IP");
    // 广播地址
    this->bcst_send_addr.sin_family      = AF_INET;
    this->bcst_send_addr.sin_port        = htons(BROADCAST_PORT);
    this->bcst_send_addr.sin_addr.s_addr = inet_addr(BROADCAST_HOST); // htonl(INADDR_BROADCAST);
    // 广播模式
    // int broadcast_enable = 1;
    // if(setsockopt(this->bcst_socket, SOL_SOCKET, SO_BROADCAST, (const char*) &broadcast_enable, sizeof(broadcast_enable)) < 0) {
    //     std::cout << "设置广播模式失败" << std::endl;
    //     return;
    // }
    // 接收超时
    timeval timeout;
    timeout.tv_sec  = 10000;
    timeout.tv_usec = 0;
    if(setsockopt(this->bcst_socket, SOL_SOCKET, SO_RCVTIMEO, (const char*) &timeout, sizeof(timeout)) < 0) {
        std::cout << "设置接收超时失败" << std::endl;
        return;
    }
    if(bind(this->bcst_socket, (sockaddr*) &this->bcst_recv_addr, sizeof(this->bcst_recv_addr)) < 0) {
        std::cout << "绑定广播通道失败" << std::endl;
        return;
    }
}

void jshare::Udp::recv() {
    if(this->recv_thread) {
        return;
    }
    this->recv_thread = std::make_shared<std::thread>([this]() {
        sockaddr_in recv_addr;
        #ifdef _WIN32
        int addr_length = sizeof(recv_addr);
        #else
        socklen_t addr_length = sizeof(recv_addr);
        #endif
        const int buffer_length = UDP_SIZE;
        std::vector<char> buffer(buffer_length);
        while(this->running) {
            const int recv_length = recvfrom(this->bcst_socket, buffer.data(), buffer_length, 0, (sockaddr*) &recv_addr, &addr_length);
            if(recv_length < HEADER_SIZE) {
                continue;
            }
            uint8_t  version     = buffer[0];
            uint8_t  type        = buffer[1];
            uint16_t data_id     = *((uint16_t*) (buffer.data() + 2));
            uint16_t data_length = *((uint16_t*) (buffer.data() + 4));
            if(version != jshare::version) {
                continue;
            }
            if(data_length > buffer_length) {
                continue;
            }
            if(type == jshare::type_ack) {
                this->ack_data_id = data_id;
                auto& packet = this->send_data[data_id % this->vector_size];
                packet.ack     = true;
                packet.overdue = true;
                std::unique_lock<std::mutex> lock(mutex);
                cond.notify_all();
                continue;
            }
            if(type == jshare::type_syn) {
                this->recv_data_id = data_id + 1;
                this->ack(data_id, recv_addr);
                continue;
            }
            if(this->callback) {
                if(type == jshare::type_broadcast) {
                    this->callback(type, buffer.data() + HEADER_SIZE, data_length, recv_addr);
                    continue;
                }
                if(data_id == this->recv_data_id) {
                    this->ack(data_id, recv_addr);
                    this->callback(type, buffer.data() + HEADER_SIZE, data_length, recv_addr);
                    ++this->recv_data_id;
                    while(true) {
                        auto& packet = this->recv_data[this->recv_data_id % this->vector_size];
                        if(packet.overdue) {
                            break;
                        }
                        this->ack(this->recv_data_id, recv_addr);
                        this->callback(packet.type, packet.data.data() + HEADER_SIZE, packet.data_length, packet.addr);
                        packet.ack     = true;
                        packet.overdue = true;
                        ++this->recv_data_id;
                    }
                } else {
                    if(data_id - this->recv_data_id >= this->vector_size) {
                        // ack
                        std::cout << "超限丢弃" << std::endl;
                        continue;
                    }
                    if(data_id < this->recv_data_id && this->recv_data_id - data_id < this->vector_size) {
                        // ack
                        continue;
                    }
                    auto& packet = this->recv_data[data_id % this->vector_size];
                    if(!packet.overdue) {
                        continue;
                    }
                    packet.type        = type;
                    packet.addr        = recv_addr;
                    packet.data_id     = data_id;
                    packet.data_length = data_length;
                    packet.ack         = false;
                    packet.overdue     = false;
                    packet.data.swap(buffer);
                }
            }
        }
    });
}

void jshare::Udp::ack(uint16_t data_id, sockaddr_in addr) {
    static uint16_t data_length = 0;
    static std::vector<char> buffer(UDP_SIZE);
    buffer[0] = jshare::version;
    buffer[1] = jshare::type_ack;
    std::memcpy(buffer.data() + 2, &data_id,     sizeof(uint16_t));
    std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
    this->send(buffer, data_length, addr, false, false);
}

bool jshare::Udp::sync(const sockaddr_in& addr) {
    uint16_t data_length = 0;
    std::vector<char> buffer(UDP_SIZE);
    buffer[0] = jshare::version;
    buffer[1] = jshare::type_syn;
    std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
    return this->send(buffer, data_length, addr, true, true);
}

bool jshare::Udp::send(std::vector<char>& data, const int data_length, const sockaddr_in& addr, bool need_ack, bool wait_ack) {
    if(need_ack) {
        uint16_t data_id = 0;
        while(this->send_data_id - this->ack_data_id >= this->vector_size / 2) {
            // TODO 动态计算
            auto& packet = this->send_data[(this->ack_data_id + 1) % this->vector_size];
            sendto(this->bcst_socket, packet.data.data(), HEADER_SIZE + packet.data_length, 0, (sockaddr*) &addr, sizeof(addr));
            // std::this_thread::yield();
            // std::this_thread::sleep_for(std::chrono::nanoseconds(10));
            // std::this_thread::sleep_for(std::chrono::milliseconds(1));
            std::unique_lock<std::mutex> lock(mutex);
            cond.wait_for(lock, std::chrono::milliseconds(10));
        }
        data_id = ++this->send_data_id;
        std::memcpy(data.data() + 2, &data_id, sizeof(uint16_t));
        const uint8_t type = data[1];
        auto& packet = this->send_data[data_id % this->vector_size];
        // while(!packet.ack) {
        //     std::this_thread::sleep_for(std::chrono::milliseconds(1));
        // }
        packet.type        = type;
        packet.addr        = addr;
        packet.data_id     = data_id;
        packet.data_length = data_length;
        packet.ack         = false;
        packet.overdue     = false;
        packet.data.swap(data);
        bool success = sendto(this->bcst_socket, packet.data.data(), HEADER_SIZE + packet.data_length, 0, (sockaddr*) &addr, sizeof(addr)) >= 0;
        if(success && wait_ack) {
            while(this->running) {
                if(packet.ack) {
                    break;
                }
                // std::this_thread::sleep_for(std::chrono::milliseconds(1));
                std::unique_lock<std::mutex> lock(mutex);
                cond.wait_for(lock, std::chrono::milliseconds(10));
            }
        }
        return success;
    } else {
        return sendto(this->bcst_socket, data.data(), HEADER_SIZE + data_length, 0, (sockaddr*) &addr, sizeof(addr)) >= 0;
    }
}

bool jshare::Udp::broadcast() {
    if(this->bcst_thread) {
        return true;
    }
    this->bcst_thread = std::make_shared<std::thread>([this]() {
        uint16_t data_length = 0;
        std::vector<char> buffer(HEADER_SIZE + data_length);
        buffer[0] = jshare::version;
        buffer[1] = jshare::type_broadcast;
        std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
        while(this->running) {
            this->send(buffer, data_length, this->bcst_send_addr, false, false);
            std::this_thread::sleep_for(std::chrono::seconds(5));
        }
    });
    return true;
}
