#include <chrono>
#include <string>
#include <iostream>

static std::string get() {
    return std::string(1024, 'a');
}

int main() {
    std::string x;
    auto a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 1'000'000; i++) {
        // std::string s = std::string(1024, 'a');
        // std::string v = std::move(s);
        std::string v = std::move(get());
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';
    a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 1'000'000; i++) {
        // std::string s = std::string(1024, 'a');
        // x = std::move(s);
        x = std::move(get());
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';
    a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 1'000'000; i++) {
        // std::string s = std::string(1024, 'a');
        // std::string v = s;
        std::string v = get();
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';
    a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 1'000'000; i++) {
        // std::string s = std::string(1024, 'a');
        // x = s;
        x = get();
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';
    return 0;
}
