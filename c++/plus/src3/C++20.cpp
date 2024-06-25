// import <iostream>
// #import <iostream>
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
    std::string path = "D:/tmp/数据";
    auto iterator = std::filesystem::directory_iterator(path);
    for(auto& entry : iterator) {
        std::cout << std::format("当前路径：{}\n", entry.path().string());
        std::cout << entry.path().string() << "\n";
        // std::cout << iterator->u8string() << "\n";
    }
    // std::cout << std::format("C++ 20 : {}\n", 20);
    return 0;
}