// import <iostream>
// #import <iostream>
#include <stdio.h>
#include <cmath>
#include <string>
#include <format>
#include <cstddef>
#include <iostream>
#include <filesystem>

int main() {
    // g++ -std=c++20
    // std::byte b{1};
    // std::isnan
    // std::isinf
    const char8_t* v = u8"测试";
    // std::u8string x = u8"测试";
    // std::cout << u8"测试";
    printf("%s\n", v);
    // std::cout << v;
    // std::cout << std::format("u8 : {}\n", v);
    std::string path = "D:/tmp/数据";
    auto iterator = std::filesystem::directory_iterator(path);
    for(auto& entry : iterator) {
        std::cout << std::format("当前路径：{}\n", entry.path().string());
        std::cout << entry.path().string() << "\n";
        // std::cout << iterator->u8string() << "\n";
    }
    // std::cout << std::format("C++ 20 : {}\n", 20);
    std::cout << std::format("{:b}\n", 42);
    std::cout << std::format("{:o}\n", 42);
    std::cout << std::format("{:d}\n", 42);
    std::cout << std::format("{:x}\n", 42);
    std::cout << std::format("{:X}\n", 42);
    // std::cout << std::format("{}\n", std::chrono::system_clock::now());
    return 0;
}