#include <iostream>

typedef void (say)(const char* message);

class Person {

public:
    void sayProxy(const char* message, say* proxy) {
        proxy(message);
    }
    void sayHihi() {
        auto lam = [this](const char* message) -> void {
            std::cout << message << "\r\n";
        };
        this->sayProxy("4321", lam);
    }

};

int main() {
    Person person;
    // person.sayProxy("1234", [](const char* message) -> void {
    //     std::cout << message << "\r\n";
    // });
    person.sayHihi();
    return 0;
}