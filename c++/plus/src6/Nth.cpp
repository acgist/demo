#include <vector>
#include <iostream>
#include <algorithm>

#define min(a, b) a < b ? a : b

int main() {
    std::vector<int> v{ 1, 2, 3, 4, 5, 6, 20, 30, 4, 7 };
    std::nth_element(v.begin(), v.begin() + 2, v.end());
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    std::vector<int> a{ 3,  30, 2 };
    std::vector<int> b{ 10, 3,  2 };
    std::vector<int> c(a.size() + b.size());
    std::merge(a.begin(), a.end(), b.begin(), b.end(), c.begin());
    std::cout << '\n';
    for(const auto& x : c) {
        std::cout << x << '\n';
    }
    auto mm = std::minmax_element(a.begin(), a.end());
    std::cout << "min = " << *mm.first  << '\n';
    std::cout << "max = " << *mm.second << '\n';
    auto [min, max] = std::minmax_element(a.begin(), a.end());
    std::cout << "min = " << *min << '\n';
    std::cout << "max = " << *max << '\n';
    std::cout << (std::min)(100, 200) << '\n';
    return 0;
}