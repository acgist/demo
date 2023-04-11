#include <iostream>

class Person {

public:
    int *age;
    long *phone;

public:
    Person(int age) {
        this->age = new int(age);
        this->phone = nullptr;
    }

    ~Person() {
        delete this->age;
        this->age = nullptr;
        // 非空不能释放两次
        // delete this->age;
        delete this->phone;
        this->phone = nullptr;
        std::cout << "析构" << std::endl;
    }

    Person& operator= (Person &p) {
        if(this->age != nullptr) {
            delete this->age;
            this->age = nullptr;
        }
        if(this->phone != nullptr) {
            delete this->phone;
            this->phone = nullptr;
        }
        // this->age = p.age;
        // this->phone = p.phone;
        this->age = new int(*p.age);
        if(this->phone != nullptr) {
            this->phone = new long(*p.phone);
        } else {
            this->phone = nullptr;
        }
        return *this;
    }

    bool operator== (Person &p) {
        return *p.age == *this->age;
    }

};

int main(int argc, char const *argv[]) {
    Person *p = new Person(18);
    delete p;
    // Person p(18);
    std::cout << "完成1" << std::endl;
    Person p1(18);
    Person p2(28);
    p2 = p1;
    std::cout << (p1 == p2) << std::endl;
    *p1.age = 10;
    std::cout << (p1 == p2) << std::endl;
    std::cout << &p1 << std::endl;
    std::cout << &p2 << std::endl;
    std::cout << p1.age << std::endl;
    std::cout << p2.age << std::endl;
    std::cout << *p1.age << std::endl;
    std::cout << *p2.age << std::endl;
    std::cout << "完成2" << std::endl;
    std::string a = std::string("1234");
    std::string b = std::string("1234");
    // std::string a = "1234";
    // std::string b = "1234";
    std::cout << &a << std::endl;
    std::cout << &b << std::endl;
    std::cout << (a == b) << std::endl;
    return 0;
}

