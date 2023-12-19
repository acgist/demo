#include "Static.hpp"

static int staticIntA = 0;

int externInt = 0;

void printA() {
    int externInt = 1111;
    static int staticIntAA = 0;
    externInt++;
    staticIntA++;
    staticIntC++;
    staticIntAA++;
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
