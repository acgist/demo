#include <iostream>
#include <functional>

void say(int a) {
    std::cout << "a = " << a << '\n';
}

int main() {
    int a = 0;
    std::cout << a << '\n';
    // auto aa = [a]() -> void {
    //     a++;
    // };
    auto aa = [a]() mutable {
        a++;
    };
    aa();
    std::cout << a << '\n';
    auto bb = [&a, x = 10]() {
        a++;
    };
    bb();
    std::cout << a << '\n';
    auto tt = [] <typename T> (T t) {
        std::cout << "t = " << t << '\n';
    };
    tt(1);
    std::invoke(say, 1);
    return 0;
}