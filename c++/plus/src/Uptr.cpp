#include <memory>
#include <iostream>

class Person {

public:
    ~Person() {
        std::cout << "1234\n";
    }

};

int main() {
    std::unique_ptr<Person> a(new Person{});
    // a.release();
    // delete a.get();
    // a.reset();
    // a = nullptr;
    std::cout << "-\n";
    return 0;
}