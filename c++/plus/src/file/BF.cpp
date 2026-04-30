#include <string>
#include <fstream>
#include <iostream>

class Person {
public:
    int age;
    std::string name;
public:
    Person() {
    }
    Person(int age, std::string name) : age(age), name(name) {
    }
};

int main(int argc, char const *argv[]) {
    // 写出
    Person person(18, "碧螺萧萧");
    // Person person(18, "acgist");
    std::ofstream out("d:/tmp/person.md", std::ios::out | std::ios::binary);
    out.write((const char*) &person, sizeof(person));
    out.close();
    // 读取
    std::ifstream in("d:/tmp/person.md", std::ios::out | std::ios::binary);
    Person clone;
    std::cout << in.good() << std::endl;
    std::cout << in.is_open() << std::endl;
    in.read((char*) &clone, sizeof(clone));
    std::cout << clone.age << std::endl;
    std::cout << clone.name << std::endl;
    in.close();
    return 0;
}
