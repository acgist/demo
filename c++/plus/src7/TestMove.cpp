#include <chrono>
#include <string>
#include <iostream>

std::string get() {
    std::string v(10240, 'v');
    return v;
}

void accept(const std::string& v) {
    if(v.size() != v.length()) {
        throw "错误";
    }
}

void accept2(std::string v) {
    if(v.size() != v.length()) {
        throw "错误";
    }
}

int main() {
    auto a = std::chrono::system_clock::now();
    for(int i = 0; i < 10'000'000; ++i) {
        accept(get());
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';

    a = std::chrono::system_clock::now();
    for(int i = 0; i < 10'000'000; ++i) {
        accept(std::move(get()));
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';

    a = std::chrono::system_clock::now();
    for(int i = 0; i < 10'000'000; ++i) {
        accept2(get());
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';

    a = std::chrono::system_clock::now();
    for(int i = 0; i < 10'000'000; ++i) {
        accept2(std::move(get()));
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';
    return 0;
}