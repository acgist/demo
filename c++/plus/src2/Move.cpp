#include <vector>
#include <iostream>

class Person {

public:
    int age = 0;
    Person() {
        std::cout << "构造????" << this->age << "\n";
    }
    Person(int age) {
        this->age = age;
        std::cout << "构造++++" << this->age << "\n";
    }
    Person(const Person& p) {
        this->age = p.age;
        std::cout << "构造****" << this->age << "\n";
    }
    ~Person() {
        std::cout << "析构----" << this->age << "\n";
    }

};

std::vector<Person> get() {
    Person p1(1);
    Person p2(2);
    // Person p3(3);
    std::cout << "1\n";
    std::vector<Person> vector;
    vector.reserve(20);
    std::cout << "2\n";
    vector.push_back(p1);
    vector.push_back(p2);
    // vector.push_back(p3);
    std::cout << "3\n";
    return vector;
    // return std::move(vector);
}

int main() {
    std::vector<Person> vector = get();
    std::cout << vector.size() << "%%%%\n";
    // std::vector<Person> copy(vector);
}
