#include "jshare/tcp.hpp"

#include <thread>
#include <vector>
#include <fstream>
#include <iostream>
#include <filesystem>

#ifdef _WIN32
#include <WS2tcpip.h>

#pragma comment(lib, "Ws2_32.lib")
#endif

void jshare::tcp::server() {
    auto tcp_socket = ::socket(AF_INET, SOCK_STREAM, 0);
    // 监听地址
    sockaddr_in addr;
    addr.sin_family      = AF_INET;
    addr.sin_port        = htons(18080);
    addr.sin_addr.s_addr = htonl(INADDR_ANY);
    // 接收超时
    timeval timeout;
    timeout.tv_sec  = 100;
    timeout.tv_usec = 0;
    if(setsockopt(tcp_socket, SOL_SOCKET, SO_RCVTIMEO, (const char*) &timeout, sizeof(timeout)) < 0) {
        std::cout << "设置接收超时失败" << std::endl;
        return;
    }
    if(bind(tcp_socket, (sockaddr*) &addr, sizeof(addr)) < 0) {
        std::cout << "绑定端口失败" << std::endl;
        return;
    }
    if(listen(tcp_socket, 10) < 0) {
        std::cout << "监听端口失败" << std::endl;
    }
    while(true) {
        auto connect = accept(tcp_socket, nullptr, nullptr);
        if(connect < 0) {
            continue;
        }
        std::thread thread([connect]() {
            int length = 0;
            const int max_length = 1024;
            std::vector<char> buffer(max_length);
            long long size = 0;
            auto a = std::chrono::system_clock::now();
            std::ofstream stream("D:/download/aliang.mp4", std::ios::out | std::ios::binary);
            while((length = recv(connect, buffer.data(), max_length, 0)) > 0) {
                stream.write(buffer.data(), length);
                size += length;
            }
            stream.close();
            auto z = std::chrono::system_clock::now();
            std::cout << "接收完毕：" << size << " = " << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << std::endl;
        });
        thread.join();
        break;
    }
    #if _WIN32
    closesocket(tcp_socket);
    #else
    close(tcp_socket);
    #endif
}

void jshare::tcp::client() {
    auto tcp_socket = ::socket(AF_INET, SOCK_STREAM, 0);
    sockaddr_in addr;
    addr.sin_family      = AF_INET;
    addr.sin_port        = htons(18080);
    addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    if(connect(tcp_socket, (sockaddr*) &addr, sizeof(addr)) < 0) {
        std::cout << "连接失败" << std::endl;
        return;
    }
    const int max_length = 1024;
    std::vector<char> buffer(max_length);
    long long size = 0;
    auto a = std::chrono::system_clock::now();
    std::ifstream stream("D:/tmp/aliang.mp4", std::ios::in | std::ios::binary);
    while(true) {
        stream.read(buffer.data(), max_length);
        auto data_length = stream.gcount();
        if(data_length == 0) {
            break;
        }
        size += data_length;
        if(send(tcp_socket, buffer.data(), data_length, 0) < 0) {
            std::cout << "发送失败" << std::endl;
        }
    }
    auto z = std::chrono::system_clock::now();
    std::cout << "发送完毕：" << size << " = " << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << std::endl;
    #if _WIN32
    closesocket(tcp_socket);
    #else
    close(tcp_socket);
    #endif
}