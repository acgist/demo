#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

int main() {
    std::string v = "1234567890";
    // v.erase()
    // std::vector<int> x{1, 2};
    std::cout << v << '\n';
    auto pos = std::remove_if(v.begin(), v.end(), [](const auto& x) { return static_cast<int>(x) % 2 == 0;});
    v.erase(pos, v.end());
    std::cout << v << '\n';
    std::erase_if(v, [](const auto& x) { return x == '3'; });
    std::cout << v << '\n';
    std::string diff = "112231123";
    auto diffpos = std::unique(diff.begin(), diff.end());
    diff.erase(diffpos, diff.end());
    std::cout << diff << '\n';
    std::vector<int> t{1, 1, 2, 3, 4, 1, 2, 3, 3, 4};
    std::cout << t.size() << '\n';
    auto posx = std::unique(t.begin(), t.end());
    t.erase(posx, t.end());
    std::cout << t.size() << '\n';
    for(const auto& i : t) {
        std::cout << i << ' ';
    }
    return 0;
}