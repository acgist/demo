#include <iostream>
#include <functional>

class P {

public:
void say() {
    std::cout << "1234\n";
}

};

int main() {
    P p;
    void (P::*say)() = P::say;
    auto x = std::mem_fn(say);
    // x(p);
    x(&p);
    return 0;
}