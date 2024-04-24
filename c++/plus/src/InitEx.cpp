#include <iostream>

class Person {

public:
    int age{};
public:
    Person() {};
    Person(int age) {
        this->age = age;
    };

};

int main() {
    Person p1;
    std::cout << p1.age << "\n";
    // Person p2();
    // std::cout << p2.age << "\n";
    Person p3{};
    std::cout << p3.age << "\n";
    Person p4(1);
    std::cout << p4.age << "\n";
    Person p5{1};
    std::cout << p5.age << "\n";
    char* charPtr = new char[1024] {0};
    std::cout << charPtr << "\n";
    std::cout << (long long) charPtr << "\n";
    charPtr = "1234";
    std::cout << charPtr << "\n";
    std::cout << (long long) charPtr << "\n";
    return 0;
}