#include <vector>
#include <string>
#include <chrono>
#include <iostream>

int main();

class Person {
friend int main();
private:
    int* age = nullptr;
    Person& operator=(Person&& p) {
        this->age = p.age;
        p.age = nullptr;
        return *this;
    }
public:
    Person() {}
    Person(int age) {}
    Person(int age, std::string name) {}
    void say1() {
        std::cout << "p say1\n";
    }
    virtual void say2() {
        std::cout << "p say2\n";
    }
};

class Man : public Person {
public:
    using Person::Person;
    using Person::say1;
    void say1(int age) {
        std::cout << "m say1\n";
    }
    void say2() {
        std::cout << "m say2\n";
    }
    void say3() {
        std::cout << "say3\n";
    }
};

int main() {
    std::vector<std::string> v1(100000);
    std::vector<std::string> v2;
    std::cout << v1.size() << " = " << v2.size() << std::endl;
    auto a = std::chrono::system_clock::now();
    // v2 = v1;
    v2 = std::move(v1);
    auto b = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(b - a).count() << std::endl;
    std::cout << v1.size() << " = " << v2.size() << std::endl;
    int aa = 1;
    int bb = 2;
    int& cc = aa;
    cc = bb;
    std::cout << aa << " = " << bb << " = " << cc << std::endl;
    aa = 10;
    bb = 20;
    cc = 30;
    std::cout << aa << " = " << bb << " = " << cc << std::endl;
    std::cout << &aa << " = " << &bb << " = " << &cc << std::endl;
    int* xx = new int{10};
    delete xx;
    xx = nullptr;
    delete xx;
    std::cout << "1234\n";
    Person p1;
    Person p2;
    std::cout << (p1.age == nullptr) << "\n";
    std::cout << (p2.age == nullptr) << "\n";
    p1.age = new int(1);
    p2 = std::move(p1);
    std::cout << (p1.age == nullptr) << "\n";
    std::cout << (p2.age == nullptr) << "\n";
    Man man;
    man.say1();
    man.say1(1);
    man.say2();
    Person& person = man;
    person.say2();
    return 0;
}