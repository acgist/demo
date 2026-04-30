#include <vector>
#include <iostream>
#include <algorithm>
#include <execution>
#include <functional>

int main() {
    std::vector<int> v{ 1, 2, 3, 4, 5, 6 };
    std::sort(std::execution::par, v.begin(), v.end(), std::greater<>{});
    for(const auto& x : v) {
        std::cout << x << " ";
    }
    return 0;
}