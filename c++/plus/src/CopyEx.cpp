#include <iostream>

class Person {

public:
    Person() {
        std::cout << "Person()\n";
    }
    Person(const Person& person) {
        std::cout << "Person(&)\n";
    }
    Person(const Person&& person) {
        std::cout << "Person(&&)\n";
    }
    ~Person() {
        std::cout << "~Person\n";
    }
    // Person& operator=(const Person& person) {
    //     std::cout << "Person=\n";
    //     return *this;
    // }

};

int main() {
    Person person;
    Person person2;
    // Person copy = person;
    // Person copy = std::move(person);
    // Person copy = Person(person);
    // Person& copyRef = person;
    person2 = person;
    return 0;
}