#include <string>
#include <thread>
#include <iostream>

class Person {

public:
    void say() {
        std::cout << this << '\n';
        std::cout << "hi\n";
    }

};

int main() {
    Person p;
    std::cout << &p << '\n';
    // std::thread t(Person::say, p);
    // std::thread t(Person::say, &p);
    std::thread t(Person::say, std::ref(p));
    // std::thread t(&Person::say, p);
    // std::thread t(&Person::say, &p);
    t.join();
    return 0;
}
