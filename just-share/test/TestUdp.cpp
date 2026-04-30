#include "jshare/udp.hpp"

#include <chrono>
#include <thread>
#include <cstring>
#include <fstream>
#include <iostream>
#include <filesystem>

int main() {
    jshare::socket::init();
    std::vector<char> bufferx(128 * 1024);
    std::ofstream out("D:/download/aliang.mp4", std::ios::out | std::ios::binary);
    out.rdbuf()->pubsetbuf(bufferx.data(), bufferx.size());
    jshare::Udp udp([&out](const int type, const char* data, const int data_length, const sockaddr_in& addr) {
        static int size = 0;
        if(type == jshare::type_file_data) {
            out.write(data, data_length);
            size += data_length;
        } else if(type == jshare::type_file_stop) {
            std::cout << "=" << size << std::endl;
            out.close();
        }
    });
    // udp.broadcast();
    sockaddr_in addr;
    addr.sin_family      = AF_INET;
    addr.sin_port        = htons(18888);
    addr.sin_addr.s_addr = inet_addr("192.168.12.68");
    std::filesystem::path filepath("D:/tmp/aliang.mp4");
    std::ifstream stream(filepath, std::ios::in | std::ios::binary);
    if(!stream.is_open()) {
        return 0;
    }
    auto a = std::chrono::system_clock::now();
    udp.sync(addr);
    std::vector<char> buffer(jshare::UDP_SIZE);
    const int max_length = jshare::UDP_SIZE - jshare::HEADER_SIZE;
    int size = 0;
    {
        const std::string name = filepath.filename().string();
        uint16_t data_length = name.length();
        buffer[0] = jshare::version;
        buffer[1] = jshare::type_file_send;
        std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
        std::memcpy(buffer.data() + 6, name.data(), data_length);
        udp.send(buffer, data_length, addr, true, true);
    }
    {
        while(true) {
            stream.read(buffer.data() + jshare::HEADER_SIZE, max_length);
            uint16_t data_length = stream.gcount();
            if(data_length == 0) {
                break;
            }
            buffer[0] = jshare::version;
            buffer[1] = jshare::type_file_data;
            std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
            udp.send(buffer, data_length, addr);
            size += data_length;
        }
    }
    {
        const std::string name = filepath.filename().string();
        uint16_t data_length = name.length();
        buffer[0] = jshare::version;
        buffer[1] = jshare::type_file_stop;
        std::memcpy(buffer.data() + 4, &data_length, sizeof(uint16_t));
        std::memcpy(buffer.data() + 6, name.data(), data_length);
        udp.send(buffer, data_length, addr, true, true);
    }
    auto z = std::chrono::system_clock::now();
    std::cout << "发送完成：" << size << " = " << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << std::endl;
    std::this_thread::sleep_for(std::chrono::days(7));
    return 0;
}
