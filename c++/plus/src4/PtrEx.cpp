#include <iostream>

class P {

public:
    int a{0};
    int say() {
        return 0;
    }

};

int main() {
    int (P::*pa) = &P::a;
    int (P::*pSay)() = P::say;
    P *p = new P{};
    p->*pa = 100;
    std::cout << p->a << '\n';
    std::cout << (p->*pSay)() << '\n';
    delete p;
    P p1;
    p1.*pa = 100;
    std::cout << p1.a << '\n';
    std::cout << (p1.*pSay)() << '\n';
    return 0;
}