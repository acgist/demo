#include <iostream>

template<typename T, typename X>
class P {

public:
    void say(T name, X age) {
        std::cout << "我是：" << name << "，今年" << age << "岁。\n";
    }

};

typedef P<const char*, int> A;

template<typename T>
using B = P<T, int>;

int main() {
    A a;
    a.say("1", 1);
    B<const char*> b;
    b.say("2", 2);
    return 0;
}