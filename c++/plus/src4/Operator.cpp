#include <iostream>

class P {

public:
    operator int() {
        return 0;
    }
    operator bool() {
        return false;
    }

};

int main() {
    P* p = new P{};
    if(p) {
        std::cout << "p ptr\n";
    }
    if(p->operator int()) {
        std::cout << "p int\n";
    }
    if(p->operator bool()) {
        std::cout << "p bool\n";
    }
    return 0;
}