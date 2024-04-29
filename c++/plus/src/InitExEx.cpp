#include <iostream>

class Person {
public:
    int age;
    const char* name;
};

struct PersonEx {
    int age;
    const char* name;
};

int main() {
    // Person p = {
    //     .age  = 10,
    //     .name = "acgist",
    // };
    // PersonEx pEx = {
    //     .age  = 10,
    //     .name = "acgist",
    // };
    // Person p1 ( 10, "age" );
    Person p1 = { 10, "age" };
    PersonEx p1Ex = { 10, "age" };
    // std::cout << p.age << "\n";
    // std::cout << pEx.age << "\n";
    std::cout << p1.name << "\n";
    std::cout << p1Ex.name << "\n";
    int age;
    std::cout << age << "\n";
    long long x = 10L;
    int v  = int( x );
    int vv = int{ x };
    std::cout << v << "\n" << vv << "\n";
    return 0;
}