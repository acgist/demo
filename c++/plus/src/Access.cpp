#include <iostream>

struct User {
    int age;
};

void access(User a, User *b, User &c);

int main(int argc, char **argv) {
    User a = {10};
    User b = {20};
    User c = {30};
    access(a, &b, c);
    std::cout << "a = " << &a << " b = " << &b << " c = " << &c << std::endl;
    std::cout << "a = " << a.age << " b = " << b.age << " c = " << c.age << std::endl;
    return 0;
}

void access(User a, User *b, User &c) {
    a.age = 100;
    b->age = 200;
//  (*b).age = 200;
    c.age = 300;
    std::cout << "a = " << &a << " b = " << b << " c = " << &c << std::endl;
    std::cout << "a = " << a.age << " b = " << b->age << " c = " << c.age << std::endl;
}

