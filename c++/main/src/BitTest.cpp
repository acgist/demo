#include <iostream>

int main() {
    int a = 15;
    std::cout << (a << 2) << std::endl;
    std::cout << (a >> 2) << std::endl;
    a = -15;
    std::cout << (a << 2) << std::endl;
    std::cout << (a >> 2) << std::endl;
}