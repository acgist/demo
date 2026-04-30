/**
 * Copyright(c) 2024-present acgist. All Rights Reserved.
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * gitee : https://gitee.com/acgist/just-share
 * github: https://github.com/acgist/just-share
 * 
 * TCP通道
 * 
 * @author acgist
 * 
 * @version 1.0.0
 */
#ifndef JSHARE_HEADER_CORE_TCP_HPP
#define JSHARE_HEADER_CORE_TCP_HPP

#if _WIN32
#include <winsock2.h>
#else
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#endif

namespace jshare::tcp {

extern void server();

extern void client();

};

#endif