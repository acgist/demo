#include <vector>
#include <iostream>
#include <algorithm>

int main() {
    std::vector<int> v{ 1, 2, 3, 4 };
    std::for_each_n(v.begin() + 1, 2, [](const auto& v) {
        std::cout << v << '\n';
    });
    return 0;
}