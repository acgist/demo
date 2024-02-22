#include <iostream>

enum Type {

    M,
    W,

};

class Person {

public:
    int age;
    Type type;
    int ageEx = 100;
    Type typeEx = Type::M;

};

int main() {
    Person person;
    std::cout << person.age  << "\r\n";
    std::cout << person.type << "\r\n";
    std::cout << person.ageEx  << "\r\n";
    std::cout << person.typeEx << "\r\n";
    return 0;
}