#include "jshare/udp.hpp"

#include <mutex>
#include <chrono>
#include <cstring>
#include <iostream>

#ifdef _WIN32
#pragma comment(lib, "Ws2_32.lib")
#endif

#define BROADCAST_PORT 18888
#define BROADCAST_HOST "255.255.255.255"

inline long long time() {
    return std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count();
}

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
    this->bcst_socket = ::socket(AF_INET, SOCK_DGRAM, 0);
    // 监听地址
    this->bcst_recv_addr.sin_family      = AF_INET;
    this->bcst_recv_addr.sin_port        = htons(BROADCAST_PORT);
    this->bcst_recv_addr.sin_addr.s_addr = htonl(INADDR_ANY); // inet_addr("IP");
    // 广播地址
    this->bcst_send_addr.sin_family      = AF_INET;
    this->bcst_send_addr.sin_port        = htons(BROADCAST_PORT);
    this->bcst_send_addr.sin_addr.s_addr = inet_addr(BROADCAST_HOST); // htonl(INADDR_BROADCAST);
    // 广播模式
    int broadcast_enable = 1;
    if(setsockopt(this->bcst_socket, SOL_SOCKET, SO_BROADCAST, (const char*) &broadcast_enable, sizeof(broadcast_enable)) < 0) {
        std::cout << "设置广播模式失败" << std::endl;
        return;
    }
    // 接收超时
    timeval timeout;
    timeout.tv_sec  = 100;
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
                this->retry();
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
                std::unique_lock<std::shared_mutex> lock(this->send_mutex);
                auto pos = this->send_data.find(data_id);
                if(pos != this->send_data.end()) {
                    this->send_data.erase(this->send_data.begin(), ++pos);
                }
                continue;
            }
            if(type == jshare::type_syn) {
                this->recv_data_id = data_id;
            }
            if(type == jshare::type_broadcast) {
                this->callback(type, buffer.data() + HEADER_SIZE, recv_length - HEADER_SIZE, recv_addr);
                continue;
            }
            bool loss = true;
            if(this->callback) {
                if(data_id == this->recv_data_id) {
                    this->callback(type, buffer.data() + HEADER_SIZE, recv_length - HEADER_SIZE, recv_addr);
                    ++this->recv_data_id;
                } else {
                    this->recv_data.emplace(data_id, jshare::Packet{
                        .type = type,
                        .time = time(),
                        .addr = recv_addr,
                        .data = std::move(buffer),
                        .size = recv_length
                    });
                    buffer.resize(buffer_length);
                }
                while(true) {
                    auto iter = this->recv_data.find(this->recv_data_id);
                    if(iter == this->recv_data.end()) {
                        break;
                    }
                    loss = false;
                    this->callback(iter->second.type, iter->second.data.data() + HEADER_SIZE, iter->second.size - HEADER_SIZE, iter->second.addr);
                    iter = this->recv_data.erase(iter);
                    ++this->recv_data_id;
                }
            }
            this->ack(this->recv_data_id - 1, recv_addr);
            if(loss) {
                this->retry();
            }
        }
    });
}

void jshare::Udp::ack(uint16_t data_id, sockaddr_in addr) {
    uint16_t data_length = 0;
    std::vector<char> buffer(HEADER_SIZE + data_length);
    buffer[0] = jshare::version;
    buffer[1] = jshare::type_ack;
    std::memcpy(buffer.data() + 2, &data_id,     sizeof(uint16_t));
    std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
    addr.sin_port = htons(BROADCAST_PORT);
    this->send(buffer, addr, false, false);
}

void jshare::Udp::retry() {
    std::shared_lock<std::shared_mutex> lock(this->send_mutex);
    auto time = ::time();
    for(auto iter = this->send_data.begin(); iter != this->send_data.end(); ++iter) {
        auto& packet = iter->second;
        if(time - packet.time > 100) {
            this->send(packet.data, packet.addr, false, false);
        } else {
            break;
        }
    }
}

bool jshare::Udp::sync(const sockaddr_in& addr) {
    uint16_t data_length = 0;
    std::vector<char> buffer(HEADER_SIZE + data_length);
    buffer[0] = jshare::version;
    buffer[1] = jshare::type_syn;
    std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
    return this->send(buffer, addr, true, true);
}

bool jshare::Udp::send(std::vector<char>& data, const sockaddr_in& addr, bool need_ack, bool wait_ack) {
    if(need_ack) {
        bool success = false;
        uint16_t data_id = 0;
        {
            std::unique_lock<std::shared_mutex> lock(this->send_mutex);
            data_id = ++this->send_data_id;
            std::memcpy(data.data() + 2, &data_id, sizeof(uint16_t));
            const uint8_t type = data[1];
            const int data_length = data.size();
            auto [ iter, ret ] = this->send_data.emplace(data_id, jshare::Packet{
                .type = type,
                .time = time(),
                .addr = addr,
                .data = std::move(data),
                .size = data_length
            });
            success = sendto(this->bcst_socket, iter->second.data.data(), iter->second.data.size(), 0, (sockaddr*) &addr, sizeof(addr)) >= 0;
        }
        do {
            {
                std::shared_lock<std::shared_mutex> lock(this->send_mutex);
                if(this->send_data.size() < 50) {
                    break;
                }
            }
            std::this_thread::sleep_for(std::chrono::milliseconds(1));
        } while(true);
        if(wait_ack && success) {
            do {
                {
                    std::shared_lock<std::shared_mutex> lock(this->send_mutex);
                    if(!this->send_data.contains(data_id)) {
                        break;
                    }
                    this->retry();
                }
                std::this_thread::sleep_for(std::chrono::milliseconds(10));
            } while(true);
        }
        return success;
    } else {
        return sendto(this->bcst_socket, data.data(), data.size(), 0, (sockaddr*) &addr, sizeof(addr)) >= 0;
    }
}

bool jshare::Udp::broadcast() {
    if(this->bcst_thread) {
        return true;
    }
    this->bcst_thread = std::make_shared<std::thread>([this]() {
        uint16_t data_length = 0;
        while(this->running) {
            std::vector<char> buffer(HEADER_SIZE + data_length);
            buffer[0] = jshare::version;
            buffer[1] = jshare::type_broadcast;
            std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
            this->send(buffer, this->bcst_send_addr, false, false);
            std::this_thread::sleep_for(std::chrono::seconds(5));
        }
    });
    return true;
}
