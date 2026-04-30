#include <string>
#include <iostream>

class P {

public:
    class T {
        public:
        void say();
    };

public:
    inline void say();

};

inline void P::say() {
}

void P::T::say() {
}

int main() {
    int array[10];
    std::string a = "1";
    std::string b = "2";
    std::string c = "3";
    std::cout << a << &a << " = " << &a[0] << "\n";
    std::cout << b << &b << " = " << &b[0] << "\n";
    std::cout << c << &c << " = " << &c[0] << "\n";
    a = c;
    b = c;
    // b = std::move(c);
    std::cout << a << &a << " = " << &a[0] << "\n";
    std::cout << b << &b << " = " << &b[0] << "\n";
    std::cout << c << &c << " = " << &c[0] << "\n";
    P p;
    p.say();
    P::T t;
    t.say();
    return 0;
}