#include <variant>
#include <iostream>
// #include <optional>
// #include <functional>

int main() {
    std::variant<int, float, double> v;
    v = 1;
    std::cout << std::holds_alternative<int>(v) << "=" << v.index() << '\n';
    v = 10.0;
    std::cout << std::holds_alternative<int>(v) << "=" << v.index() << '\n';
    std::cout << std::get<2>(v) << '\n';
    std::cout << std::get<double>(v) << '\n';
    int* v1 = std::get_if<0>(&v);
    int* v2 = std::get_if<int>(&v);
    std::cout << (v1 == nullptr) << '\n';
    std::cout << (v2 == nullptr) << '\n';
    // std::optional<int> x;
    // std::reference_wrapper<int> ref;
    return 0;
}
