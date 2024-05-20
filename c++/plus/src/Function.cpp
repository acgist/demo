#include <iostream>
#include <functional>

void x3(std::function<int()>* fun) {
    std::cout << (long long)(fun) << "\n";
    (*fun)();
}

void x2(std::function<int()>& fun) {
    std::cout << (long long)(&fun) << "\n";
    fun();
    x3(&fun);
}

void x1(std::function<int()> fun) {
    std::cout << (long long)(&fun) << "\n";
    fun();
    x2(fun);
    // x2(fun);
}

int main() {
    x1([]() {
        return 0;
    });
    return  0;
}