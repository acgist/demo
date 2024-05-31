#include <iostream>

class Person {

friend std::ostream& operator<<(std::ostream&, const Person&);

private:
    const char* name = nullptr;

public:
    Person(const char* name) {
        this->name = name;
    }
    virtual ~Person() {
    }

};

std::ostream& operator<<(std::ostream& out, const Person& person) {
    out << person.name;
    return out;
}

int main() {
    Person person("1234");
    std::cout << person;
    return 0;
}