#include <iostream>
#include <optional>

int main() {
    std::optional<int> i = std::nullopt;
    std::cout << i.has_value() << '\n';
    std::cout << i.value_or(1111) << '\n';
    std::optional<int> x = 1;
    std::cout << x.value() << '\n';
    std::cout << x.has_value() << '\n';
    std::optional<int> c = std::move(x);
    // std::optional<int> c(std::move(x));
    // std::optional<int> c{std::move(x)};
    std::cout << "====\n";
    std::cout << x.value_or(0) << '\n';
    std::cout << x.has_value() << '\n';
    std::cout << c.value_or(0) << '\n';
    std::cout << c.has_value() << '\n';
    return 0;
}