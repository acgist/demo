#include <iostream>

int main() {
    auto   a = 1.0F;
    auto   b = 2.0;
    float  c = 3.0;
    double d = 4.0;
    auto   e = 1L;
    auto   f = 1;
    long long g = 1L;
    std::cout << sizeof(a) << std::endl;
    std::cout << sizeof(b) << std::endl;
    std::cout << sizeof(c) << std::endl;
    std::cout << sizeof(d) << std::endl;
    std::cout << sizeof(e) << std::endl;
    std::cout << sizeof(f) << std::endl;
    std::cout << sizeof(g) << std::endl;
    return 0;
}