#include <string>
#include <iostream>

class T {

public:
    std::string name;

public:
    T() {
        std::cout << "T()\n";
    }
    T(const T& t) {
        std::cout << "T(T&)\n";
    }
    T(T&& t) {
        this->name.swap(t.name);
        std::cout << "T(T&&)\n";
    }
    T& operator= (const T& t) {
        std::cout << "=(T&)\n";
        return *this;
    }
    T& operator= (T&& t) {
        std::cout << "=(T&&)\n";
        return *this;
    }

};

class P {

public:
    T t;

};

P getP() {
    T t;
    t.name = "name";
    P p {
        t
        // std::move(t)
    };
    std::cout << "====" << t.name << '\n';
    std::cout << "====" << p.t.name << '\n';
    return p;
}

int main() {
    P p = getP();
    return 0;
}
