#include <random>
#include <vector>
#include <iostream>
#include <algorithm>

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
    std::vector<Person> vector{};
    vector.reserve(4);
    vector.push_back(Person(0));
    vector.push_back(Person(1));
    vector.push_back(Person(3));
    std::cout << "-------------\n";
    vector.push_back(Person(4));
    // vector.insert(vector.begin() + 2, Person(5));
    // for(auto iterator = vector.begin(); iterator < vector.end(); ++iterator) {
    //     std::cout << iterator->age << "\n";
    // }
    std::vector<Person> copy{};
    std::cout << &copy << "\n";
    std::cout << &vector << "\n";
    vector.swap(copy);
    std::cout << copy.size() << "\n";
    std::cout << vector.size() << "\n";
    std::cout << &copy << "\n";
    std::cout << &vector << "\n";
    // std::random_shuffle
    std::random_device device;
    std::mt19937 rand(device());
    std::shuffle(copy.begin(), copy.end(), rand);
}
