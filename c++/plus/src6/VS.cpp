#include <set>
#include <vector>
#include <iostream>

int main() {
    std::set<int> s{ 1, 2, 3 };
    std::vector<int> v;
    for(const auto& x : s) {
        std::cout << x << '\n';
    }
    std::cout << "====\n";
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    std::cout << "============================\n";
    // v.resize(s.size());
    v.reserve(s.size());
    // v.assign(s.begin(), s.end());
    // v.assign(std::move(s.begin()), std::move(s.end()));
    // v.insert(v.begin(), s.begin(), s.end());
    // v.insert(v.begin(), std::move(s.begin()), std::move(s.end()));
    for(const auto& x : s) {
        std::cout << x << '\n';
    }
    std::cout << "====\n";
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    return 0;
}
