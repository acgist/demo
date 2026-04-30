#include <iostream>

class Person {

private:
    int i = 0;

public:
    Person() {
    }
    Person(int i) : i(i) {
    }
    Person(const Person& p) {
        std::cout << p.i << " = " << "&\n";
    }
    Person(const Person&& p) {
        std::cout << p.i << " = " << "&&\n";
    }

};

static Person get(int i) {
    Person p(i);
    return p;
}

int main() {
    // Person p1 { get(1) };
    // Person p2 { std::move(get(2)) };
    // Person&& p3 { get(3) };
    // Person&& p4 { std::move(get(4)) };
    Person p1 = get(1);
    Person p2 = std::move(get(2));
    Person&& p3 = get(3);
    Person&& p4 = std::move(get(4));
    return 0;
}