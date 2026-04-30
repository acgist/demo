#include <iostream>

class P {

public:
    int a = 0;
    int* b = 0;
    P() {
        std::cout << "P\n";
    };
    P(const P& p) {
        std::cout << "P&\n";
    };

};

class M {

public:
    P p;
    M() {
        std::cout << "M\n";
    };
    M(const M& m) {
        std::cout << "M&\n";
    };
    // M& operator=(const M& m) {
    //     std::cout << "M=\n";
    //     return *this;
    // }

};

int main() {
    int a = 1000;
    int b = 1000;
    P p1;
    p1.a = 100;
    p1.b = &a;
    M m1;
    m1.p = p1;
    P p2;
    p2.a = 200;
    p2.b = &b;
    M m2;
    m2.p = p2;
    std::cout << &m1.p << " = " << m1.p.a << " = " << m1.p.b << '\n';
    std::cout << &m2.p << " = " << m2.p.a << " = " << m2.p.b << '\n';
    std::cout << "====\n";
    m2 = m1;
    std::cout << &m1.p << " = " << m1.p.a << " = " << m1.p.b << '\n';
    std::cout << &m2.p << " = " << m2.p.a << " = " << m2.p.b << '\n';
    return 0;
}