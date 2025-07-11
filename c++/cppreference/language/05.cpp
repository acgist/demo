#include <vector>
#include <numeric>
#include <iostream>

int main() {
    std::vector<int> v(10);
    std::iota(v.begin(), v.end(), 0);
    for(const auto& x : v) {
        std::cout << x << std::endl;
    }
    return 0;
}