#include <iostream>

class P {

public:
    int age = 0;
    [[nodiscard]] bool operator==(const P& p) const {
        return this->age == p.age;
    }
    std::partial_ordering operator<=>(const P& p) const {
        return this->age <=> p.age;
    }

};

int main() {
    P p1;
    P p2;
    p1.age = 100;
    p2.age = 10;
    std::cout << (p1 > p2) << '\n';
    std::cout << (p1 == p2) << '\n';
    std::cout << (p1 != p2) << '\n';
    return 0;
}