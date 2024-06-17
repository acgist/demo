#include <iostream>
#include <algorithm>

class P {

public:
    // static P p;
    P() {
        std::cout << "P\n";
    }
    ~P() {
        std::cout << "~P\n";
    }

};

static P p;

int main() {
    // std::max(1, 2);
    std::cout << "main\n";
    return 0;
}