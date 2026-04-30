#include <vector>
#include <cstring>
#include <iostream>

int main() {
    std::vector<int> v{1, 2, 3, 4};
    std::copy(v.begin() + 1, v.end(), v.begin());
    // std::memcpy(v.data(), v.data(), 0);
    // std::copy(v.begin() + 1, v.begin() + 2, v.begin());
    for(auto& x : v) {
        std::cout << x << '\n';
    }
    return 0;
}