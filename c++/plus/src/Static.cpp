#include "Static.hpp"

const int constIntB = 0;

static int staticIntA = 0;
static int staticIntB = 0;

// int c = 0;
const int d = 0;

int externInt = 0;

void printA() {
    int externInt = 1111;
    static int staticIntAA = 0;
    externInt++;
    staticIntA++;
    staticIntC++;
    staticIntAA++;
    std::cout << "constIntA    = " << constIntA   << std::endl;
    std::cout << "constIntB    = " << constIntB   << std::endl;
    std::cout << "constIntB    = " << &constIntB  << std::endl;
    std::cout << "externInt    = " << externInt   << std::endl;
    std::cout << "::externInt  = " << ::externInt << std::endl;
    std::cout << "staticIntA   = " << staticIntA  << std::endl;
    std::cout << "staticIntAA  = " << staticIntAA << std::endl;
    std::cout << "staticIntC   = " << staticIntC  << std::endl;
}

int main() {
    printA();
    std::cout << "====" << std::endl;
    printA();
    std::cout << "====" << std::endl;
    printB();
    std::cout << "====" << std::endl;
    printB();
    return 0;
}
