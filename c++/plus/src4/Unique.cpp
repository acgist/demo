#include <memory>
#include <iostream>

class P {

public:
    ~P() {
        std::cout << "~P\n";
    }

};

int main() {
    std::unique_ptr<P> p1{new P{}};
    std::cout << "----1\n";
    p1.get();
    // p1.reset();
    // p1.release();
    std::cout << "----2\n";
    return 0;
}