#include <iostream>

class Person {

public:
    int age = 100;
    static int maxAge;

};

int Person::maxAge = 200;

int main() {
    Person p1;
    Person p2;
    Person* pPtr = new Person;
    p1.maxAge = 300;
    std::cout << p1.age << " = " << p1.maxAge << "\n";
    std::cout << p2.age << " = " << p2.maxAge << "\n";
    std::cout << pPtr->age << " = " << pPtr->maxAge << "\n";
    pPtr->maxAge = 200;
    std::cout << p1.age << " = " << p1.maxAge << "\n";
    std::cout << p2.age << " = " << p2.maxAge << "\n";
    std::cout << pPtr->age << " = " << pPtr->maxAge << "\n";
    return 0;
}