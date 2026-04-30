#include <iostream>

class P /* final */ {

public:
    P() {};
    P(int a) {};
    void p(int v);
    void p(const char* v);
    virtual void p(double v);
    void say() {
        std::cout << "P say\n";
    }

};

void P::p(int v) {
    std::cout << "int P.p = " << v << '\n';
}

void P::p(const char* v) {
    std::cout << "const char* P.p = " << v << '\n';
}

void P::p(double v) {
    std::cout << "double P.p = " << v << '\n';
}

class A {

public:
    void say() {
        std::cout << "A say\n";
    }

};

class M : public P, public A {

public:
    using P::p;
    using P::P;
    virtual void p(int v)    /* override */;
    virtual void p(double v) override;
    // void say() {
    //     std::cout << "M say\n";
    // }
    using P::say;

};

void M::p(int v) {
    std::cout << "int M.p = " << v << '\n';
}

void M::p(double v) {
    std::cout << "double M.p = " << v << '\n';
}

int main() {
    M m;
    m.p(1);
    m.p("1234");
    // m.say();
    m.say();
    m.P::say();
    m.A::say();
    P& p {m};
    p.p(1);
    return 0;
}