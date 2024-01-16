#include "Static.hpp"

static int staticIntA = 0;
static int staticIntB = 0;

// int c = 0;
const int d = 0;

void printB() {
    static int staticIntBB = 0;
    externInt++;
    staticIntB++;
    staticIntC++;
    staticIntBB++;
    std::cout << "constIntA      = " << constIntA   << std::endl;
    std::cout << "constIntB      = " << constIntB   << std::endl;
    std::cout << "constIntB      = " << &constIntB  << std::endl;
    std::cout << "ex externInt   = " << externInt   << std::endl;
    std::cout << "ex staticIntB  = " << staticIntB  << std::endl;
    std::cout << "ex staticIntBB = " << staticIntBB << std::endl;
    std::cout << "ex staticIntC  = " << staticIntC  << std::endl;
}
