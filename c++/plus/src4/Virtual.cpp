#include <iostream>

class A {

public:
    A() {};
    virtual ~A() {
        std::cout << "~A\n";
    };
    virtual void say() {
        std::cout << "A.say\n";
    }

};

class B : public A {

public:
    B() {};
    ~B() {
        std::cout << "~B\n";
    };
    void say() {
        std::cout << "B.say\n";
    }

};

class C : public B {

public:
    C() {};
    ~C() {
        std::cout << "~C\n";
    };
    void say() {
        std::cout << "C.say\n";
    }

};

int main() {
    // C c;
    // c.say();
    A* a = new C{};
    a->say();
    delete a;
    return 0;
}