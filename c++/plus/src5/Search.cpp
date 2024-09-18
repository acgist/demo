#include <vector>
#include <string>
#include <iostream>
#include <algorithm>
#include <functional>

int main() {
    // std::vector<int> v{2, 3};
    std::vector<int> v{1, 8, 2, 3, 4, 5};
    // std::vector<int> x{2, 3};
    std::vector<int> x{1, 8, 2, 4, 5};
    std::default_searcher s(x.begin(), x.end());
    auto ret = std::search(v.begin(), v.end(), s);
    for(; ret != v.end(); ++ret) {
        std::cout << *ret << '\n';
    }
    auto m = std::mismatch(x.begin(), x.end(), v.begin());
    std::cout << *m.first  << '\n';
    std::cout << *m.second << '\n';
    return 0;
}