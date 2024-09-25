#include <vector>
#include <iostream>

int main() {
    const std::vector<int> v { 1, 2, 3 };
    auto b = v.begin();
    const auto e = v.end();
    for(; b != e; ++b) {
        std::cout << *b << '\n';
    }
    return 0;
}