#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

void print(int v) {
    std::cout << v << std::endl;
}

class Person {
public:
    // char* name;
    std::string name;
    int age;
    // Person(std::string name, int age) {
    Person(std::string& name, int& age) {
    // Person(const std::string& name, const int& age) {
        this->name = name;
        this->age  = age;
    }
};

int main(int argc, char const *argv[]) {
    std::vector<int> vector;
    vector.push_back(0);
    vector.push_back(1);
    vector.push_back(2);
    vector.push_back(3);
    std::vector<int>::iterator iterator = vector.begin();
    while(iterator < vector.end()) {
        std::cout << *iterator << std::endl;
        iterator++;
    }
    std::cout << vector.at(0) << std::endl;
    std::cout << vector.empty() << std::endl;
    vector.erase(vector.begin());
    // std::for_each(vector.begin(), vector.end(), print);
    int* const pCount = new int(0);
    int count = 0;
    // []() {}
    // []() -> int {}
    std::for_each(vector.begin(), vector.end(), [&count](int& v) {
        std::cout << v << std::endl;
        count += v;
    });
    std::for_each(vector.begin(), vector.end(), [pCount](int& v) {
        std::cout << v << std::endl;
        *pCount = *pCount + v;
    });
    std::cout << count << std::endl;
    std::cout << *pCount << std::endl;
    // char* ppan = "aa";
    std::string ppan = "aa";
    int ppaa = 1;
    Person ppa(ppan, ppaa);
    // std::string& vv = "acgist";
    // const std::string& vv = "acgist";
    // Person ppa("aa", 1);
    // Person ppb("bb", 2);
    // Person ppc("cc", 3);
    std::vector<Person> vp;
    vp.push_back(ppa);
    // vp.push_back(ppb);
    // vp.push_back(ppc);
    ppa.name = "ffff";
    ppa.age = 1000;
    // ppan = "ffff";
    for(Person& v : vp) {
    // for(auto v : vp) {
        std::cout << v.name << " = " << v.age << std::endl;
    }
    return 0;
}
