#include <iostream>

class Person {

public:
    bool v = false;
    operator bool() {
        return v;
    }

};

int main() {
    Person p1;
    Person p2;
    p2.v = true;
    std::cout << (bool) p1 << "\n";
    std::cout << (bool) p2 << "\n";
    return 0;
}