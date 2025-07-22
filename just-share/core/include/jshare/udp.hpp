/**
 * Copyright(c) 2024-present acgist. All Rights Reserved.
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * gitee : https://gitee.com/acgist/just-share
 * github: https://github.com/acgist/just-share
 * 
 * UDP通道
 * 
 * @author acgist
 * 
 * @version 1.0.0
 */
#ifndef JSHARE_HEADER_CORE_UDP_HPP
#define JSHARE_HEADER_CORE_UDP_HPP

#if _WIN32
#include <winsock2.h>
#else
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#endif

#include <map>
#include <memory>
#include <string>
#include <thread>
#include <vector>
#include <functional>
#include <shared_mutex>

namespace jshare {

namespace socket {

    extern bool init(); // 加载

};

const uint8_t version  = 1;
const uint8_t type_ack = 1;
const uint8_t type_syn = 2;
const uint8_t type_broadcast = 10;
const uint8_t type_file_send = 20;
const uint8_t type_file_data = 21;
const uint8_t type_file_stop = 22;

const int UDP_SIZE    = 1472;
const int HEADER_SIZE = 6;

struct Packet {

    uint8_t           type; // 类型
    long long         time; // 时间戳
    sockaddr_in       addr; // 地址
    std::vector<char> data; // 数据
    int               size; // 数据长度

};

class Udp {

private:
    #if _WIN32
    SOCKET bcst_socket = 0;
    #else
    int bcst_socket = 0;
    #endif
    bool running = false;
    sockaddr_in bcst_recv_addr;
    sockaddr_in bcst_send_addr;
    uint16_t send_data_id = 0;
    uint16_t recv_data_id = 0;
    std::shared_mutex recv_mutex;
    std::shared_mutex send_mutex;
    std::map<uint16_t, Packet> recv_data;
    std::map<uint16_t, Packet> send_data;
    std::shared_ptr<std::thread> recv_thread{ nullptr };
    std::shared_ptr<std::thread> bcst_thread{ nullptr };
    std::function<void(const int type, const char* data, const int data_length, const sockaddr_in& addr)> callback{ nullptr };

public:
    Udp(std::function<void(const int, const char*, const int, const sockaddr_in&)> callback);
    ~Udp();

private:
    void init();
    void recv();
    void ack(uint16_t data_id, sockaddr_in addr);
    void retry();

public:
    bool sync(const sockaddr_in& addr);
    bool send(std::vector<char>& data, const sockaddr_in& addr, bool need_ack = true, bool wait_ack = false); // 发送数据
    bool broadcast(); // 广播

};

};

#endif