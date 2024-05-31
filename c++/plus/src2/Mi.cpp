#include <iostream>

class A {

public:
int age = 0;
int get() {
    return this->age;
}

};

class B : /* virtual */ public A {

public:
int age = 1;
int get() {
    return this->age;
}

};

class C : /* virtual */ public A {

public:
int age = 2;
int get() {
    return this->age;
}

};

class D : public B, public C {

public:
int age = 4;
int get() {
    return this->age;
}

};

int main() {
    A a;
    std::cout << "a = " << a.get() << "\n";
    B b;
    std::cout << "b = " << b.get() << "\n";
    C c;
    std::cout << "c = " << c.get() << "\n";
    // D d;
    // std::cout << "d = " << d.get() << "\n";
    D* d = new D {};
    // A* ad = (A*) d;
    B* bd = (B*) d;
    C* cd = (C*) d;
    // std::cout << "ad = " << ad->get() << "\n";
    std::cout << "bd = " << bd->get() << "\n";
    std::cout << "cd = " << cd->get() << "\n";
    std::cout << "d = " << d->get() << "\n";
}