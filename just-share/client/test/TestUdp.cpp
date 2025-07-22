#include "jshare/udp.hpp"

#include <chrono>
#include <thread>
#include <cstring>
#include <fstream>
#include <iostream>
#include <filesystem>

int main() {
    jshare::socket::init();

    jshare::Udp udp([](const int type, const char* data, const int data_length, const sockaddr_in& addr) {
        // char* ip = inet_ntoa(addr.sin_addr);
        // static int i = 0;
        // std::cout << "消息数量 = " << ++i << std::endl;
        // std::cout << "消息类型 = " << type << std::endl;
        // std::cout << "消息长度 = " << data_length << std::endl;
        // std::cout << "消息内容 = " << data << std::endl;
        // std::cout << "消息来源 = " << ip << ":" << addr.sin_port << std::endl;
        // static std::ofstream stream("D:/download/264.mp4", std::ios::out | std::ios::binary);
        static std::ofstream stream("D:/download/aliang.mp4", std::ios::out | std::ios::binary);
        // static std::ofstream stream("D:/download/idcard.jpg", std::ios::out | std::ios::binary);
        if(type == jshare::type_file_data) {
            stream.write(data, data_length);
        } else if(type == jshare::type_file_stop) {
            stream.close();
        }
    });
    // udp.broadcast();
    uint16_t data_length = 10;
    std::vector<char> buffer(jshare::HEADER_SIZE + data_length);
    buffer[0] = jshare::version;
    buffer[1] = jshare::type_broadcast;
    std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
    std::memcpy(buffer.data() + 6, "123321123", 10);
    sockaddr_in addr;
    addr.sin_family      = AF_INET;
    addr.sin_port        = htons(18888);
    addr.sin_addr.s_addr = inet_addr("192.168.12.123");
    auto a = std::chrono::system_clock::now();

    // int i = 0;
    // udp.sync(addr);
    // while(++i <= 1000) {
    //     auto copy = buffer;
    //     udp.send(copy, addr);
    //     // udp.send(copy, addr, true, true);
    // }

    // std::filesystem::path filepath("D:/tmp/264.mp4");
    std::filesystem::path filepath("D:/tmp/aliang.mp4");
    // std::filesystem::path filepath("D:/tmp/idcard.jpg");
    std::ifstream stream(filepath, std::ios::in | std::ios::binary);
    if(!stream.is_open()) {
        return 0;
    }
    udp.sync(addr);
    {
        const std::string name = filepath.filename().string();
        uint16_t data_length = name.length();
        std::vector<char> buffer(jshare::HEADER_SIZE + data_length);
        buffer[0] = jshare::version;
        buffer[1] = jshare::type_file_send;
        std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
        std::memcpy(buffer.data() + 6, name.data(), data_length);
        udp.send(buffer, addr, true, true);
    }
    {
        const uint16_t max_length = jshare::UDP_SIZE - jshare::HEADER_SIZE;
        std::vector<char> buffer(jshare::HEADER_SIZE + max_length);
        while(true) {
            stream.read(buffer.data() + jshare::HEADER_SIZE, max_length);
            uint16_t data_length = stream.gcount();
            if(data_length == 0) {
                break;
            }
            buffer[0] = jshare::version;
            buffer[1] = jshare::type_file_data;
            std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
            if(data_length != max_length) {
                buffer.resize(jshare::HEADER_SIZE + data_length);
            }
            udp.send(buffer, addr);
            buffer.resize(jshare::HEADER_SIZE + max_length);
        }
    }
    {
        const std::string name = filepath.filename().string();
        uint16_t data_length = name.length();
        std::vector<char> buffer(jshare::HEADER_SIZE + data_length);
        buffer[0] = jshare::version;
        buffer[1] = jshare::type_file_stop;
        std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
        std::memcpy(buffer.data() + 6, name.data(), data_length);
        udp.send(buffer, addr, true, true);
    }

    auto z = std::chrono::system_clock::now();
    std::cout << "发送完成：" << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << std::endl;
    std::this_thread::sleep_for(std::chrono::days(7));
    return 0;
}
