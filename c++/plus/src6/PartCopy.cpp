#include <vector>
#include <iostream>
#include <iterator>
#include <algorithm>

int main() {
    std::vector<int> v{ 2, 1, 3, 4, 0, 8, 9 };
    std::vector<int> a;
    std::vector<int> b;
    a.resize(v.size());
    b.resize(v.size());
    auto pos = std::partition_copy(v.begin(), v.end(), a.begin(), b.begin(), [](const auto& v) {
        return v % 2 == 0;
    });
    a.erase(pos.first,  a.end());
    b.erase(pos.second, b.end());
    std::copy(a.begin(), a.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << '\n';
    std::copy(b.begin(), b.end(), std::ostream_iterator<int>(std::cout, " "));
    std::vector<int> c{ 1, 2, 3 };
    std::vector<int> d{ 3, 2, 1 };
    std::vector<int> e{ 1, 2, 1 };
    std::cout << '\n';
    std::cout << std::is_sorted(c.begin(), c.end()) << '\n';
    std::cout << std::is_sorted(d.begin(), d.end(), std::less<>{}) << '\n';
    std::cout << std::is_sorted(d.begin(), d.end(), std::greater<>{}) << '\n';
    std::cout << std::is_sorted(e.begin(), e.end()) << '\n';
    return 0;
}