#include <iomanip>
#include <iostream>

int main() {
    std::cout << (0.01 / 10000) << '\n';
    std::cout << std::fixed << std::setprecision(4) << (0.01 / 10000) << '\n';
    return 0;
}