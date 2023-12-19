#include "Static.hpp"

static int staticIntB = 0;

void printB() {
    static int staticIntBB = 0;
    externInt++;
    staticIntB++;
    staticIntC++;
    staticIntBB++;
    std::cout << "ex externInt   = " << externInt   << std::endl;
    std::cout << "ex staticIntB  = " << staticIntB  << std::endl;
    std::cout << "ex staticIntBB = " << staticIntBB << std::endl;
    std::cout << "ex staticIntC  = " << staticIntC  << std::endl;
}
