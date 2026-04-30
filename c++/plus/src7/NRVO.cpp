#include <string>
#include <iostream>

class T {

public:
    std::string name;

public:
    T() {
        std::cout << "T()\n";
    }
    T(const std::string& name) : name(name) {
    }
    T(const T& t) {
        std::cout << "T(T&)\n";
    }
    T(T&& t) {
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

bool get() {
    return false;
}

T getT() {
    T t1;
    T t2;
    if(get()) {
        return t1;
    } else {
        return t2;
    }
    // return get() ? t1 : t2;
    // return get() ? std::move(t1) : std::move(t2);
}

int main() {
    T t = getT();
    return 0;
}