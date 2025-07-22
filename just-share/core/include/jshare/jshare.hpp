/**
 * Copyright(c) 2024-present acgist. All Rights Reserved.
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * gitee : https://gitee.com/acgist/just-share
 * github: https://github.com/acgist/just-share
 * 
 * 终端暴露发现
 * 文件发送接收
 * 
 * @author acgist
 * 
 * @version 1.0.0
 */
#ifndef JSHARE_HEADER_CORE_JSHARE_HPP
#define JSHARE_HEADER_CORE_JSHARE_HPP

#include <tuple>
#include <string>
#include <vector>
#include <functional>

#include "jshare/udp.hpp"

namespace jshare {

enum class Mode {

NONE,
RECV,
SEND,

};

struct Remote {

    std::string host; // 地址
    int         port; // 端口

    bool operator == (const Remote& remote) {
        return this->host == remote.host;
    }

};

class Client {

public:
    Mode        mode = Mode::NONE; // 当前模式
    std::string path = "";         // 保存路径
    std::vector<std::string>    file;    // 文件列表
    std::vector<jshare::Remote> remote;  // 远程终端
    std::shared_ptr<Udp> udp{ nullptr }; // UDP通道
    std::function<void(const std::string&, int, bool)> recv_callback{ nullptr }; // 接收回调
    std::function<void(const std::string&, int, bool)> send_callback{ nullptr }; // 发送回调

public:
    Client(const std::string& path);
    ~Client();

public:
    bool recv(std::function<void(const std::string&, int, bool)> callback);                                  // 接收文件
    bool send(const Remote& remote, std::function<void(const std::string&, int, bool)> callback);            // 发送文件
    std::vector<Remote> remote_list();                                                                       // 终端列表
    bool file_put(const std::string& file);                                                                  // 添加文件
    bool file_cls();                                                                                         // 清空文件
    std::vector<std::string> file_list();                                                                    // 文件列表
    bool reset();                                                                                            // 重置终端
    bool close();                                                                                            // 关闭终端
    void udp_callback(const uint8_t type, const char* data, const int data_length, const sockaddr_in& addr); // UDP回调

};

};

#endif
