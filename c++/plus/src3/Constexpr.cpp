#include <iostream>

int getSize() {
    return 32;
}

constexpr int getSizeConst() {
    return 32;
}

constexpr /* const */ int size = 1 + 1;

constexpr int getSizeExpr(int a) {
    return a * 100;
}

consteval int getSizeEval(int a) {
    return a * 100;
}

int main() {
    // size = 4;
    // int array1[getSize()];
    int array2[getSizeConst()];
    int array3[size];
    // std::cout << std::size(array1) << '\n';
    std::cout << std::size(array2) << '\n';
    std::cout << std::size(array3) << '\n';
    std::cout << getSizeExpr(11) << '\n';
    std::cout << getSizeExpr(12) << '\n';
    std::cout << getSizeEval(11) << '\n';
    std::cout << getSizeEval(12) << '\n';
    return 0;
}