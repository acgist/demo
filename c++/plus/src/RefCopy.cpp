#include <iostream>

class Person {

public:
    Person() {
        std::cout << "+" << std::endl;
    }
    Person(const Person& person) {
        std::cout << "&" << std::endl;
    }
    ~Person() {
        std::cout << "~" << std::endl;
    }
    Person& operator=(const Person& person) {
        std::cout << "=" << std::endl;
        return *this;
    }

};

Person& ref() {
    Person* pPtr = new Person;
    return *pPtr;
};

Person copy() {
    Person* pPtr = new Person;
    return *pPtr;
};

int main() {
    // Person person;
    // Person p1 = person;
    // Person p2 = person;
    // Person& p3 = person;
    // Person p1 = ref();
    // Person p2 = copy();
    Person& p1 = ref();
    Person p2 = p1;
    // Person&& p2 = copy();
    // Person p = p2;
    return 0;
}