#include <vector>
#include <ranges>
#include <iostream>

int main() {
    std::vector<int> v{ 1, 2, 3, 4 };
    auto c {
        v |
        std::ranges::views::filter([](const auto& i) { return i % 2 == 0; }) |
        std::ranges::views::reverse |
        std::ranges::views::take(1)
    };
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    std::cout << "====" << '\n';
    for(const auto& x : c) {
        std::cout << x << '\n';
    }
    return 0;
}