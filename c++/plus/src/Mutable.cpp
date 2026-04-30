#include <string>
#include <iostream>

class Mutable {

public:
    mutable int age;
    // std::string name;
    const char* name;
    Mutable() {
        this->age = 0;
        this->name = "1234";
    }

};

int main() {
    Mutable a;
    a.age  = 1;
    a.name = "acgist";
    std::cout << a.age << std::endl;
    std::cout << a.name << std::endl;
    const Mutable b;
    b.age  = 1;
    // b.name = "acgist";
    std::cout << b.age << std::endl;
    std::cout << b.name << std::endl;
    return 0;
}
