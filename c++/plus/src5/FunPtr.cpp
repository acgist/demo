#include <iostream>

bool isEq(int a, int b) {
    return a == b;
}

void eqSay(bool eq, int a, int b) {
    if(eq) {
        std::cout << "相等\n";
    } else {
        std::cout << "不等\n";
    }
}

using isEqFun  = bool(*)(int, int);
using eqSayFun = void(*)(bool, int, int);

class P {

public:
    void eqSay(bool eq, int a, int b) {
        if(eq) {
            std::cout << "相等\n";
        } else {
            std::cout << "不等\n";
        }
    }

};

void execute(int a, int b, isEqFun isEqFunPtr, eqSayFun eqSayFunPtr) {
    eqSayFunPtr(isEqFunPtr(a, b), a, b);
}

void execute(int a, int b, isEqFun isEqFunPtr, void(P::*pf)(bool, int, int), P& p) {
    (p.*pf)(isEqFunPtr(a, b), a, b);
}

int main() {
    // execute(1, 1, isEq, eqSay);
    // execute(1, 2, &isEq, &eqSay);
    P p;
    // void(P::*pf)(bool, int, int) = &P::eqSay;
    // (p.*pf)(true, 1, 1);
    execute(1, 2, &isEq, &P::eqSay, p);
    return 0;
}