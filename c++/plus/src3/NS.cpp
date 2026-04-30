#include <string>
#include <iostream>

int main() {
    auto i = std::atoi("1234d");
    std::cout << i << '\n';
    std::cout << std::stoi("99", nullptr, 10) << '\n';
    std::cout << std::stoi("99", nullptr, 32) << '\n';
    // std::cout << std::stoi("99", nullptr, 64) << '\n';
    std::string a = "hello";
    std::string_view b{ a + " world!" };
    std::cout << a << '\n';
    std::cout << b << '\n';
    return 0;
}