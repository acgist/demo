#include <string>
#include <iostream>

using namespace std;

struct User {
    int age;
    string name;
} c;

struct Size {
    int age;
    long long id;
};

struct SizeInt {
    int age;
};

struct SizeLong {
    long long id;
    long long age;
};

int main(int argc, char **argv) {
    struct User a;
    cout << a.name << endl;

    struct User b = { 20, "init" };
    cout << "b name = " << b.name << endl;

    c.name = "pop init";
    cout << "c name = " << c.name << endl;

    // struct可以省略
    User user;
    user.age = 10;
    user.name = "acgist";
    cout << "user name = " << user.name << endl;

    cout << sizeof(User) << endl;
    cout << sizeof(user) << endl;
    cout << sizeof(Size) << endl; // 4 + 8 + 4 ?
    cout << sizeof(SizeInt) << endl; // 4
    cout << sizeof(SizeLong) << endl; // 8 + 8

    User users[] = { {10, "a"}, {20, "b"} };
    for(int i = 0; i < sizeof(users) / sizeof(User); i++) {
        cout << "array" << '.' << i << " = " << users[i].name << endl;
    }

    User *pUsers = users;
    for(int i = 0; i < sizeof(users) / sizeof(User); i++) {
//      cout << "array" << '.' << i << " = " << *pUsers.name << endl;
//      cout << "array" << '.' << i << " = " << (*pUsers).name << endl;
        cout << "array" << '.' << i << " = " << pUsers->name << endl;
        pUsers++;
    }


}
