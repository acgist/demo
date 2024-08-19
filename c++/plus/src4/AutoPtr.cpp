#include <memory>
#include <iostream>

class P {

public:
    ~P() {
        std::cout << "~P\n";
    }

};

int main() {
    std::unique_ptr<P> ptr{ new P{} };
    std::cout << "====\n";
    ptr = std::make_unique<P>();
    std::cout << "====\n";
    return 0;
}