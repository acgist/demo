#include <chrono>
#include <string>
#include <vector>
#include <iostream>

std::vector<std::string> get() {
    return {
        "1",
        "2",
        "3",
        "4",
    };
}

int main() {
    auto a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 1'000'000; ++i) {
        // auto v{ get() };
        auto v = get();
        // auto v{ std::move(get()) };
        // auto v = std::move(get());
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';
    return 0;
}
