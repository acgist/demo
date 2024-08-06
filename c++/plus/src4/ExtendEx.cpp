#include <iostream>

class A {

public:
    A() {
        std::cout << "A\n";
    }
    ~A() {
        std::cout << "~A\n";
    }

};

class B : public A {

public:
    B() {
        std::cout << "B\n";
    }
    ~B() {
        std::cout << "~B\n";
    }

};

int main() {
    B b;
    return 0;
}