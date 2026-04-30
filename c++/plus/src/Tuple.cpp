#include <iostream>

int main() {
    std::tuple<int, int> v{ 1, 2 };
    std::cout << std::get<0>(v) << "\n";
    std::cout << std::get<1>(v) << "\n";
    auto [ a, b ] = v;
    std::cout << a << "\n";
    std::cout << b << "\n";
    auto& [ c, d ] = v;
    std::cout << c << "\n";
    std::cout << d << "\n";
    return 0;
}