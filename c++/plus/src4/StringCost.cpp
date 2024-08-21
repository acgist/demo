#include <string>
#include <chrono>
#include <iostream>

std::string aa(const std::string& s) {
    return s;
}

std::string bb(const std::string& s) {
    return std::move(s);
}

int main() {
    auto a = std::chrono::system_clock::now();
    for(int i = 0; i < 100'000; ++i) {
        std::string v = aa("1234");
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(z - a).count() << '\n';
    a = std::chrono::system_clock::now();
    for(int i = 0; i < 100'000; ++i) {
        std::string v = bb("1234");
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(z - a).count() << '\n';
    a = std::chrono::system_clock::now();
    for(int i = 0; i < 100'000; ++i) {
        std::string&& v = aa("1234");
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(z - a).count() << '\n';
    a = std::chrono::system_clock::now();
    for(int i = 0; i < 100'000; ++i) {
        std::string&& v = bb("1234");
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::nanoseconds>(z - a).count() << '\n';
    return 0;
}