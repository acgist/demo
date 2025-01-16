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
        std::cout << "T(T&&)\n";
    }
    T& operator= (const T& person) {
        std::cout << "=(T&)\n";
        return *this;
    }
    T& operator= (T&& person) {
        std::cout << "=(T&&)\n";
        return *this;
    }

};

// T getT() {
//     return T{};
// }

T getT() {
    T t;
    return t;
}

int main() {
    // -fno-elide-constructors
    T t = std::move(getT());
    return 0;
}
