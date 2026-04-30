#include <iostream>

using std::cout;
using std::endl;

class Animal {
public:
    // 地址早绑定
    // 
    virtual void run() {
        cout << "animal run" << endl;
    }
};
class Cat : public Animal {
public:
    void run() override {
        // this->Animal::run();
        cout << "Cat run" << endl;
    }
};
class Dog : public Animal {
public:
    void run() {
        // this->Animal::run();
        cout << "Dot run" << endl;
    }
};

void run(Animal* animal) {
    animal->run();
}

void run2(Animal animal) {
    animal.run();
}

void run3(Animal& animal) {
    animal.run();
}

int main(int argc, char const *argv[]) {
    Animal animal;
    run(&animal);
    Cat cat;
    run(&cat);
    Dog dog;
    run(&dog);
    run2(dog);
    run3(dog);
    return 0;
}

