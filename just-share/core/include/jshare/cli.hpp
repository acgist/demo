/**
 * Copyright(c) 2024-present acgist. All Rights Reserved.
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * gitee : https://gitee.com/acgist/just-share
 * github: https://github.com/acgist/just-share
 * 
 * 命令
 * 
 * @author acgist
 * 
 * @version 1.0.0
 */
#ifndef JSHARE_HEADER_CORE_CLI_HPP
#define JSHARE_HEADER_CORE_CLI_HPP

#include <string>

namespace jshare {
    
    extern void init(const std::string& path, int argc, char const *argv[]); // 加噪系统
    extern void kyin(); // 键盘输入
    extern void exit(); // 退出系统

};

#endif