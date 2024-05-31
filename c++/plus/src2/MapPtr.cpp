#include <map>
#include <iostream>

class Person {
public:
    int age = 0;
    Person(int age) {
        this->age = age;
        std::cout << this->age << "构造1++++\n";
    }
    Person(const Person& p) {
        this->age = p.age;
        std::cout << this->age << "构造2++++\n";
    }
    ~Person() {
        std::cout << this->age << "析构----\n";
    }
};

int main() {
    std::map<int, Person> map;
    auto p1 = Person(1);
    auto p2 = Person(2);
    std::cout << &p1 << "\n";
    auto x = map.emplace(1, p1);
    map.emplace(2, p2);
    std::cout << &(map.at(1)) << "\n";
    std::cout << &x.first->second << "\n";
    std::cout << x.first->first << "\n";
    std::cout << x.first->second.age << "\n";
    return 0;
}