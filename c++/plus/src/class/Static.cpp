#include <iostream>

using std::cout;
using std::endl;

class Person {
public:
    static int age;
    static int getAge() {
        Person::age++;
        return 100;
    }
};

int Person::age = 100;

class Size {
public:
    int  a = 0;
    bool b;
    static int c;
    void d() {
    }
    Size add(int size) {
        this->a += size;
        return *this;
    }
    Size& addRef(int size) {
        this->a += size;
        return *this;
    }
};

int Size::c = 100;

int main(int argc, char const *argv[]) {
    Person person;
    cout << person.age << endl;
    cout << person.getAge() << endl;
    cout << Person::age << endl;
    cout << Person::getAge() << endl;
    Size size;
    // 没有成员内存大小为一
    cout << sizeof(size) << endl;
    size.add(1).add(1);
    cout << size.a << endl;
    size.addRef(1).addRef(1);
    cout << size.a << endl;
    return 0;
}
