#include "jshare/cli.hpp"

#include <memory>
#include <string>
#include <iostream>

#include "jshare/udp.hpp"
#include "jshare/jshare.hpp"

static void jshare_recv();        // 接收模式
static void jshare_select_file(); // 选择发送文件
static void jshare_select_recv(); // 选择接收终端

static std::shared_ptr<jshare::Client> client{ nullptr };

inline void clear_screen() {
    #ifdef _WIN32
    std::system("cls");
    #elif defined __linux__
    std::system("clear");
    #elif defined __APPLE__
    #elif defined __unix__
    #else
    #endif
}

void jshare::init(const std::string& path, int argc, char const *argv[]) {
    std::cout << "当前路径：" << path << std::endl;
    jshare::socket::init();
    client = std::make_shared<jshare::Client>(path);
    if(argc > 1) {
        std::string mode = argv[0];
        if(mode == "-r" || mode == "--recv") {
            client->mode = jshare::Mode::RECV;
        } else if(mode == "-s" || mode == "--send") {
            client->mode = jshare::Mode::SEND;
        } else {
            // -
        }
    }
    if(argc > 2) {
        for(int i = 2; i < argc; ++i) {
            client->file_put(argv[i]);
        }
    }
}

void jshare::kyin() {
    if(client->mode == jshare::Mode::RECV) {
        jshare_recv();
    } else if(client->mode == jshare::Mode::SEND) {
        jshare_select_recv();
    } else {
        std::cout << R"(请根据命令提示进行操作
1. 接收模式
2. 发送模式
0. 退出系统
)" << std::endl;
        std::string line;
        if(std::getline(std::cin, line)) {
            if(line == "1") {
                clear_screen();
                jshare_recv();
            } else if(line == "2") {
                clear_screen();
                jshare_select_file();
            } else if(line == "0") {
                jshare::exit();
            } else {
                clear_screen();
                jshare::kyin();
            }
        }
    }
}

void jshare::exit() {
    client->close();
    std::exit(0);
}

static void jshare_recv() {
    client->recv([](const std::string& name, int speed, bool finish) {
        std::cout << "\r" << name << " -> " << speed;
        if(finish) {
            std::cout << "\n接收完成" << std::endl;
        }
    });
    std::cout << R"(请根据命令提示进行操作
1. 结束接收
0. 退出系统
)" << std::endl;
    std::cout << "接收文件目录：" << client->path << std::endl;
    std::string line;
    if(std::getline(std::cin, line)) {
        if(line == "1") {
            client->reset();
            clear_screen();
            jshare::kyin();
        } else if(line == "0") {
            jshare::exit();
        } else {
            clear_screen();
            jshare_recv();
        }
    }
}

static void jshare_select_file() {
    std::cout << R"(请根据命令提示进行操作
1. 列出选择文件列表
2. 清空选择文件列表
3. 完成选择
9. 重选模式
0. 退出系统
每次输入一个文件完整路径回车
)" << std::endl;
    std::string line;
    read:
    if(std::getline(std::cin, line)) {
        if(line.empty()) {
            clear_screen();
            jshare_select_file();
        } else if(line == "1") {
            clear_screen();
            auto list = client->file_list();
            for(const auto& file : list) {
                std::cout << file << '\n';
            }
            if(!list.empty()) {
                std::cout << std::endl;
            }
            jshare_select_file();
        } else if(line == "2") {
            clear_screen();
            client->file_cls();
            jshare_select_file();
        } else if(line == "3") {
            clear_screen();
            jshare_select_recv();
        } else if(line == "9") {
            client->reset();
            clear_screen();
            jshare::kyin();
        } else if(line == "0") {
            jshare::exit();
        } else {
            client->file_put(line);
            std::cout << "添加成功继续..." << std::endl;
            goto read;
        }
    }
}

static void jshare_select_recv() {
    static int  base = 100;
    static auto list = client->remote_list();
    for(int i = 0; i < list.size(); ++i) {
        std::cout << (base + i) << ". " << list[i].host << '\n';
    }
    if(!list.empty()) {
        std::cout << std::endl;
    }
    std::cout << R"(请根据命令提示进行操作
1. 重新扫描
2. 重选文件
9. 重选模式
0. 退出系统
输入序号选择发送终端开始发送
)" << std::endl;
    std::string line;
    if(std::getline(std::cin, line)) {
        if(line == "1") {
            clear_screen();
            jshare_select_recv();
        } else if(line == "2") {
            clear_screen();
            jshare_select_file();
        } else if(line == "9") {
            client->reset();
            clear_screen();
            jshare::kyin();
        } else if(line == "0") {
            jshare::exit();
        } else {
            int index = std::atoi(line.c_str()) - base;
            if(index < 0 || index > list.size()) {
                std::cout << "终端无效" << std::endl;
            } else {
                std::cout << "开始发送" << std::endl;
                client->send(list[index], [](const std::string& name, int speed, bool finish) {
                    std::cout << "\r" << name << " -> " << speed;
                    if(finish) {
                        std::cout << "\n发送完成" << std::endl;
                    }
                });
            }
            jshare_select_recv();
        }
    }
}
