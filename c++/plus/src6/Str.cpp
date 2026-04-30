#include <iostream>

int main() {
    const char* v = "1 2 3 4";
    char* x;
    std::cout << std::strtod(v, &x) << '\n';
    std::cout << std::strtod(x, &x) << '\n';
    std::cout << std::strtod(x, &x) << '\n';
    std::cout << std::strtod(x, &x) << '\n';
    std::cout << std::strtod(x, &x) << '\n';
    std::cout << std::strtod(x, &x) << '\n';
    std::cout << std::strtod(x, &x) << '\n';
    // std::strtok
    return 0;
}