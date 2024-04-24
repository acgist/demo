#include <memory>
#include <cstring>
#include <iostream>

class Person {

public:
    int age;

public:
    Person() {};
    Person(int age) {
        this->age = age;
    }
    Person(const Person& person) {
        std::cout << "c\n";
        this->age = person.age;
    }
    ~Person() {
        std::cout << "~\n";
    }
    Person& operator=(const Person& person) {
        if(this == &person) {
            return *this;
        }
        std::cout << "=\n";
        // Person ret;
        // return ret;
        return *this;
    }

};

class PersonWrapper {

public:
    std::unique_ptr<Person> personUPtr = nullptr;

public:
    ~PersonWrapper() {}

};

int main() {
    // Person p1;
    // Person p2 = p1;
    // Person p3;
    // p3 = p2;
    PersonWrapper* pwPtr = new PersonWrapper;
    pwPtr->personUPtr = std::unique_ptr<Person>(new Person);
    pwPtr->personUPtr.reset();
    // char* chars = new char[10] { 0, 0, 0, 0 };
    // char* chars = new char[10] { '0', '0', '0', '0' };
    char* chars = new char[10] { '\0', '\0', '\0', '\0' };
    std::cout << chars << "\n";
    chars[0] = '1';
    chars[1] = '2';
    char& v = chars[1];
    v = 'v';
    std::cout << chars << "\n";
    std::cout << std::strlen(chars) << "\n";
    // pwPtr->personUPtr.release();
    // delete pwPtr;
    // char* x;
    char* x = nullptr;
    // char* x = new char;
    delete x;
    // delete[] x;
    std::cout << "1234\n";
    return 0;
}