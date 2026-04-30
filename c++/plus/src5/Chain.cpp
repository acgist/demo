#include <ranges>
#include <vector>
#include <iostream>

int main() {
    std::vector<int> vector = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    auto ret = vector
        | std::views::filter([](int i) { return i % 2 == 0; })
        | std::views::transform([](int i) { return i * 2; })
        | std::views::take(3);
    for (const auto& v : ret) {
        std::cout << v << ' ';
    }
    return 0;
}