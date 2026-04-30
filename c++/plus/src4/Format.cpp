#include <string>
#include <format>
#include <iostream>

int main() {
    std::string x = std::format("1234{}", 1);
    std::cout << x << '\n';
    return 0;
}