#include <vector>
#include <iostream>

int main() {
    std::vector<int> v;
    v.push_back(1);
    v.push_back(2);
    v.push_back(3);
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    // v.pop_back();
    v.erase(v.begin());
    std::cout << "====\n";
    for(const auto& x : v) {
        std::cout << x << '\n';
    }
    std::vector<int> size(10);
    size.push_back(1);
    std::cout << size.size() << '\n';
    size.reserve(100);
    std::cout << size.size() << '\n';
    size.resize(20);
    std::cout << size.size() << '\n';
    return 0;
}