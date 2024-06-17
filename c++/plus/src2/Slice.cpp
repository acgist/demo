#include <iostream>

class P {

public:
    int a = 10;
    virtual ~P() {
        std::cout << "~P\n";
    }

};

class X : public P {

public:
    int b = 20;
    ~X() {
        std::cout << "~X\n";
    }

};

int main() {
    X* x = new X();
    P* p = x;
    std::cout << p->a << "\n";
    std::cout << x->a << "\n";
    std::cout << x->b << "\n";
    delete p;
    std::cout << p->a << "\n";
    std::cout << x->a << "\n";
    std::cout << x->b << "\n";
    return 0;
}