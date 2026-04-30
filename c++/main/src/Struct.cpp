#include <string>
#include <iostream>

struct Person {
    std::string name;
    int         age;
    int         amount : 2;
};

enum Color {
    red,
    blue,
    green,
};

int main(int argc, char const *argv[]) {
    // 含有括号将会初始为零
    Person person {};
    // 不要括号将会有有脏数据
    // Person person;
    std::cout << person.name << std::endl;
    std::cout << person.age << std::endl;
    Person acgist = {
        "acgist",
        9999
    };
    // Person acgist {
    //     "acgist",
    //     9999
    // };
    std::cout << acgist.name << std::endl;
    std::cout << acgist.age << std::endl;
    // Person* blxx = new Person();
    Person* blxx = new Person {
        "碧螺萧萧",
        // 逗号也行
        100,
    };
    std::cout << blxx->name << std::endl;
    std::cout << blxx->age << std::endl;
    delete blxx;
    // delete blxx;
    // Person array[10];
    Person array[10] {};
    std::cout << array[2].name << std::endl;
    std::cout << array[2].age << std::endl;
    std::cout << array[2].amount << std::endl;
    std::cout << sizeof(blxx->age) << std::endl;
    // std::cout << sizeof(blxx->amount) << std::endl;
    Color a = Color::red;
    Color b = Color(1);
    Color c = Color(((int) a) + ((int) b));
    std::cout << a << std::endl;
    std::cout << b << std::endl;
    std::cout << c << std::endl;
    return 0;
}
