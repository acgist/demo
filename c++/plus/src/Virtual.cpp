#include <iostream>

class Person {

public:
    Person() {};
    virtual ~Person() {};

public:
    virtual void say() = 0;

};

class Man : public Person {

public:
    Man() {};
    virtual ~Man() {};

public:
    void say() override {
        std::cout << "1\n";
    }

};

int main() {
    Person* man = new Man();
    man->say();
    delete man;
    return 0;
}