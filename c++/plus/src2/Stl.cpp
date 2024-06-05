#include <vector>
#include <iostream>
#include <algorithm>

class Person {

public:
    ~Person() {
        std::cout << "析构\n";
    }

};

int main() {
    // double* a = new double[] { 1, 0, 4, 3 };
    double a[4] { 1, 0, 4, 3 };
    std::sort(a, a + 4);
    for(auto& v : a) {
        std::cout << v << "\n";
    }
    // delete[] a;
    // std::vector<Person> v;
    // v.reserve(2);
    // v.push_back(Person{});
    // v.push_back(Person{});
    std::vector<Person*> v;
    v.reserve(2);
    v.push_back(new Person{});
    v.push_back(new Person{});
    // v.~vector<Person*>();
    // (&v)->~vector<Person*>();
    std::cout << "====\n";
    return 0;
}