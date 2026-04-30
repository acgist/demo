#include <variant>
#include <iostream>

int main() {
    std::cout << std::variant_size_v<std::variant<int>> << '\n';
    std::variant<std::monostate, int> a;
    std::cout << a.index() << '\n';
    std::variant<int, char> b = 0B00000000'00000000'10000000'00000001;
    std::cout << std::get<int>(b) << '\n';
    std::cout << std::get<0>(b) << '\n';
    std::variant<int, double, char> c;
    std::cout << c.index() << '\n';
    c = 1;
    c = 'x';
    c = 1.0;
    std::cout << c.index() << '\n';
    return 0;
}