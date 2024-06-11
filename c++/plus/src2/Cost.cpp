#include <chrono>
#include <string>
#include <iostream>

class A {

};

class B : public A {

};

void funA(A* a) {
    std::cout << "a* is nullptr = " << (a == nullptr) << "\n";
}

void funB(B* b) {
    std::cout << "b* is nullptr = " << (b == nullptr) << "\n";
}

int main() {
    A a;
    B b;
    funA(&a);
    funA(&b);
    // funB(&a);
    funB(&b);
    A* bb = dynamic_cast<A*>(&b);
    // B* bb = dynamic_cast<B*>(&a);
    std::cout << "bb* is nullptr = " << (bb == nullptr) << "\n";
    std::string str;
    std::cout << str.length() << "\n";
    auto aa = std::chrono::system_clock::now();
    for(int i = 0; i < 100'000; ++i) {
        std::string xx;
    }
    auto zz = std::chrono::system_clock::now();
    auto diff = std::chrono::duration_cast<std::chrono::milliseconds>(zz - aa).count();
    std::cout << diff << "\n";
    return 0;
}