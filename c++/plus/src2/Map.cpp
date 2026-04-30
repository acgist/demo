#include <map>
#include <string>
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
    std::cout << "1\n";
    // map.insert(std::pair<int, Person>{ 1, p1 });
    map.emplace(1, p1);
    std::cout << "2\n";
    // map.insert({ 2, p2 });
    map.emplace_hint(map.begin(), 2, p2);
    std::cout << "3\n";
    // std::map<int, Person> copy(map.begin(), map.end());
    // std::map<int, Person> copy(std::move(map));
    std::map<int, Person> copy;
    std::cout << copy.size() << " = " << map.size() << "\n";
    std::swap(copy, map);
    std::cout << copy.size() << " = " << map.size() << "\n";
    return 0;
}