#include <iostream>

class Person {

public:
    int age;
    virtual int getAge() = 0;

};

class Man : public Person {

public:
    Man() = default;
    Man(const Man&) = delete;
    Man& operator= (const Man&) = delete;
    virtual int getAge() {
        return this->age;
    }

};

int main(int argc, char const *argv[]) {
    Man man;
    man.age = 100;
    Person& person = man;
    std::cout << man.getAge() << "\n";
    std::cout << person.getAge() << "\n";
    // Man deleteMan = man;
    // std::cout << deleteMan.getAge() << "\n";
    return 0;
}

