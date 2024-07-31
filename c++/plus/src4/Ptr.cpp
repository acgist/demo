#include <memory>
#include <iostream>

class P {

public:
    int say() {
        std::cout << "P say\n";
        return 0;
    };
    int say() const {
        std::cout << "P say const\n";
        return 0;
    };

};

int main() {
    int a = 0;
    std::unique_ptr<int> b = std::make_unique<int>(a); 
    std::unique_ptr<int> c { &a };
    std::cout << &a << '\n'; 
    std::cout << b.get() << '\n'; 
    std::cout << c.get() << '\n'; 
    c.release();
    const P p;
    p.say();
    return 0;
}