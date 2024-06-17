#include <iostream>

class P {
public:
    virtual int age() {
        return 10;
    }
};

class X {
public:
    virtual int age() {
        return 20;
    }
};

class H {

public:
    int say() {
        return 100;
    }
    int say(int x) {
        return x * 100;
    }

};

class PX : public X, public P, public H {
public:
    int age() {
        return 30;
    }
    using H::say;
    int say(const char* t) {
        return 0;
    }
};

class PPX : private X, private P {
public:
    int age() {
        return 30 + X::age();
    }
};

int main() {
    PX px;
    std::cout << px.say() << "\n";
    std::cout << px.H::say() << "\n";
    std::cout << px.age() << "\n";
    std::cout << px.X::age() << "\n";
    PPX ppx;
    std::cout << ppx.age() << "\n";
    // std::cout << ppx.X::age() << "\n";
    return 0;
}