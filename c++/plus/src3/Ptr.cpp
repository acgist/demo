#include <iostream>

class P {
public:
    ~P() {
        std::cout << "~P\n";
    }
};

void build(P* p) {
    p = new P{};
}

void build(P** p) {
    // delete p;
    delete *p;
    *p = new P{};
}

int main() {
    P* p = nullptr;
    build(p);
    std::cout << (p == nullptr) << "\n";
    build(&p);
    std::cout << (p == nullptr) << "\n";
    build(&p);
    std::cout << (p == nullptr) << "\n";
    return 0;
}