#include <iostream>

class O {

public:
    O() {
        std::cout << "O\n";
    }

};

class P {

public:
    P() {
        std::cout << "P\n";
    }

};

class Q {

public:
    O o;
    P p;
    Q(const O& o, const P& p) : p(p) {
        std::cout << "QQQQ\n";
        this->o = o;
        std::cout << "Q\n";
    }

};

int main() {
    O o;
    P p;
    std::cout << "====\n";
    Q q{o, p};
    return 0;
}