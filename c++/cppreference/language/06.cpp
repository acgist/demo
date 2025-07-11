#include <memory>
#include <iostream>

class X {};

int main() {
    auto x = std::make_shared<X>();
    std::cout << (typeid(*x) == typeid(X)) << std::endl;
    std::cout << (typeid(*x) == typeid(int)) << std::endl;
    return 0;
}