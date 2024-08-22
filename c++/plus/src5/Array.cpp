#include <array>
#include <iostream>

int main() {
    std::array<int, 10> array{};
    // std::array<int, 10> array{ 0 };
    // std::array<int, 10> array{ 10, 10 };
    for(const auto& v : array) {
        std::cout << v << '\n';
    }
    return 0;
}