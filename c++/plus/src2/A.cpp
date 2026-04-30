#include "A1.hpp"
#include "A2.hpp"

#include <iostream>

const int a = 100;

class A {

public:
    static const int I = 3;
    static const float F;
    static const double D;

};

static const float O = 3.14F;

const float A::F = 3.14F;
const double A::D = 3.14;

int main() {
    std::cout << a << "\n";
    std::cout << A::I << "\n";
    std::cout << A::F << "\n";
    std::cout << A::D << "\n";
    std::cout << O << "\n";
    return 0;
}