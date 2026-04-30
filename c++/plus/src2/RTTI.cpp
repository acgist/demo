#include <iostream>
#include <typeinfo>

class Person {

public:
    virtual void say() {
        std::cout << "Person\n";
    }

};

class Man : public Person {

public:
    void say() {
        std::cout << "Man\n";
    }

};

int main() {
    // Person p;
    // const auto& id = typeid(Person);
    // std::cout << id.name() << "\n";
    Person* p = new Person;
    Man*    m = new Man;
    p->say();
    m->say();
    Person* pp = dynamic_cast<Person*>(m);
    pp->say();
    Man* mm = dynamic_cast<Man*>(p);
    if(mm == nullptr) {
        std::cout << "nullptr\n";
    } else {
        mm->say();
    }
    const int* a = new int(0);
    std::cout << a << "\n";
    int* b = const_cast<int*>(a);
    std::cout << b << "\n";
    // *a = 10;
    *b = 100;
    std::cout << *a << "\n";
    std::cout << *b << "\n";
    b = new int(10);
    std::cout << b << "\n";
    const int c = 100;
    int* d = const_cast<int*>(&c);
    std::cout << c << "\n";
    std::cout << *d << "\n";
    *d = 1;
    std::cout << c << "\n";
    std::cout << *d << "\n";
    return 0;
}