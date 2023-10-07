#include <vector>
#include <iostream>
#include <algorithm>

class FindOne {

public:
    bool operator()(const int& v) const {
        return v == 1;
    }

};

class Person {
public:
    int age;
    Person(int age) {
        this->age = age;
    }
    Person() {
    }
    Person(const Person& person) {
        this->age = person.age;
    }
public:
    bool operator==(const Person& person) const {
        std::cout << (long long) this << " = " <<  (long long) &person << std::endl;
        return &person == this;
        // return person.age == this->age;
    }
};

int main(int argc, char const *argv[]) {
    std::vector<int> v;
    v.push_back(1);
    v.push_back(2);
    v.push_back(3);
    std::vector<int>::iterator find = std::find_if(v.begin(), v.end(), [](const int& v) -> bool { return v == 1; });
    // std::vector<int>::iterator find = std::find_if(v.begin(), v.end(), [](int& v) { return v == 1; });
    // std::vector<int>::iterator find = std::find_if(v.begin(), v.end(), FindOne());
    if(find == v.end()) {
        std::cout << "没有找到" << std::endl;
    } else {
        std::cout << "找到数据：" << *find << std::endl;
    }
    find = std::find(v.begin(), v.end(), 20);
    if(find == v.end()) {
        std::cout << "没有找到" << std::endl;
    } else {
        std::cout << "找到数据：" << *find << std::endl;
    }
    int a;
    // int b();
    int c(1);
    int d{1};
    std::vector<Person> persons;
    Person pa(1);
    Person pb(2);
    Person pc(3);
    Person pd(3);
    Person pe(4);
    persons.push_back(pa);
    persons.push_back(pb);
    persons.push_back(pc);
    Person& copy = pa;
    std::cout << (long long) &pa   << " " << pa.age   << std::endl;
    std::cout << (long long) &copy << " " << copy.age << std::endl;
    std::vector<Person>::iterator personFind = std::find(persons.begin(), persons.end(), pb);
    std::cout << (personFind == persons.end()) << std::endl;
    persons.resize(10);
    personFind = std::find(persons.begin(), persons.end(), pd);
    std::cout << (personFind == persons.end()) << std::endl;
    personFind = std::find(persons.begin(), persons.end(), pe);
    std::cout << (personFind == persons.end()) << std::endl;
    return 0;
}
