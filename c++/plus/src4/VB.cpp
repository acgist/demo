#include <iostream>

class A {

public:
    void say() {
        std::cout << "A say\n";
    }

};

class B : public virtual A {

// public:
//     void say() {
//         std::cout << "B say\n";
//     }

};

class C : public virtual A {

// public:
//     void say() {
//         std::cout << "C say\n";
//     }

};

class D : public /* virtual */ B, public /* virtual */ C {

// public:
//     void say() {
//         std::cout << "D say\n";
//     }

};

int main() {
    D d;
    d.say();
    d.A::say();
    d.B::say();
    d.C::say();
    return 0;
}
