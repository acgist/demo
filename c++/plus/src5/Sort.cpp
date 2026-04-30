#include <ranges>
#include <vector>
#include <iostream>
#include <algorithm>

class Person {

public:
    int age = 0;

public:
    Person(int age) {
        this->age = age;
    }

};

int main() {
    // std::vector<int> vector;
    // vector.push_back(int{2});
    // vector.push_back(int{4});
    // vector.push_back(int{2});
    // std::sort(vector.begin(), vector.end());
    // std::ranges::sort(vector);
    // std::vector<Person> vector;
    // vector.push_back(Person{2});
    // vector.push_back(Person{4});
    // vector.push_back(Person{2});
    // std::sort(vector.begin(), vector.end(), [](const auto& a, const auto& z) { return a.age > z.age; });
    // for(const auto& v : vector) {
    //     std::cout << v.age << '\n';
    // }
    std::vector<Person> vector;
    vector.push_back(Person{2});
    vector.push_back(Person{4});
    vector.push_back(Person{2});
    std::ranges::sort(vector, {}, &Person::age);
    for(const auto& v : vector) {
        std::cout << v.age << '\n';
    }
    return 0;
}