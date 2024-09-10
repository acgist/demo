#include <iostream>
#include <functional>

static void funa(int a, int& b) {
    ++b;
    std::cout << a << " = " << b << '\n';
}

int main() {
    int b = 2;
    // auto fun = std::bind(funa, std::placeholders::_1, b);
    // std::function<void(int)> fun = std::bind(funa, std::placeholders::_1, b);
    std::function<void(int)> fun = std::bind(funa, std::placeholders::_1, std::ref(b));
    fun(1);
    std::cout << b << '\n';
    return 0;
}