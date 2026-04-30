#include <vector>
#include <iostream>

int main() {
    std::vector<int> v{1, 2, 3};
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    std::cout << "====\n";
    v.resize(10, 100);
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    std::cout << "====\n";
    v.resize(2);
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    return 0;
}
