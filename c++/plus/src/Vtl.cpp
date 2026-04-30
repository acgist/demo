#include <iostream>

class Person {

public:
    int age;
    const char* name;
    virtual void say() {
        std::cout << "person\n";
    }

};

class Man : public Person {

public:
    virtual void say() {
        Person::say();
        std::cout << "hi man\n";
    }

};

int main() {
    // Person man = Man();
    // man.say();
    Man man;
    Person& manRef = man;
    // man.say();
    manRef.say();
    return 0;
}