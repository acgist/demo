#include <iostream>

class Person {

public:
    int* a = new int(100);
    ~Person() {
        delete a;
        std::cout << "~\n";
    }
    void name() {
        std::cout << a << " = " << (*a) << " = name\n";
    }

};

static void get(Person** p) {
    Person person{};
    *p = &person;
    (*p)->name();
    std::cout << (*p) << '\n';
}

int main() {
    Person* p = nullptr;
    std::cout << "1\n";
    get(&p);
    std::cout << "2\n";
    p->name();
    *p->a = 400;
    p->name();
    std::cout << p << '\n';
    return 0;
}