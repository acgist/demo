#include <iostream>

class Person {
public:
    virtual ~Person() {
        std::cout << "~person\n";
    }
};

class Man : public Person {
public:
    virtual ~Man() {
        std::cout << "~man\n";
    }
};

class LMan : public Man {
public:
    virtual ~LMan() {
        // 析构函数自动调用
        // Man::~Man();
        std::cout << "~lman\n";
    }
};

int main() {
    // Man man;
    // LMan lman;
    // Person* person = new LMan{};
    // delete person;
    Man* man = new LMan{};
    delete man;
    return 0;
}
