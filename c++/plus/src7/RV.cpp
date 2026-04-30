#include <iostream>

class Person {

public:
    Person() {
        std::cout << "*\n";
    }
    Person(const Person& p) {
        std::cout << "*&\n";
    }
    Person(Person&& p) {
        std::cout << "*&&\n";
    }
    Person operator=(const Person& p) {
        std::cout << "=&\n";
        return {};
    }
    Person operator=(Person&& p) {
        std::cout << "=&&\n";
        return {};
    }
    Person operator+(const Person& p) {
        std::cout << "+\n";
        return {};
    }

};

// void set(Person p) {
//     std::cout << "set\n";
// }

void set(Person&& p) {
    std::cout << "set&&\n";
}

void set(const Person& p) {
    std::cout << "set&\n";
}

int main() {
    Person p;
    set(p);
    std::cout << "====\n";
    set(p + p);
    std::cout << "====\n";
    set(std::move(p));
    return 0;
}