#include "./Def.hpp"

#include <iostream>

int main() {
    std::cout << a << std::endl;
    std::cout << b << std::endl;
    a = 100;
    b = 100;
    std::cout << getA() << std::endl;
    std::cout << getB() << std::endl;
    std::cout << a << std::endl;
    std::cout << b << std::endl;
    print();
    getA();
    return 0;
}