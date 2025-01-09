#include <iostream>
#include <functional>

static void funa(int a, int& b, int c) {
    ++b;
    std::cout << a << " = " << b << " = " << c << '\n';
}

int main() {
    int b = 2;
    // auto fun = std::bind(funa, std::placeholders::_1, b);
    // std::function<void(int)> fun = std::bind(funa, std::placeholders::_1, b);
    std::function<void(int, int)> fun = std::bind(funa, std::placeholders::_2, std::ref(b), std::placeholders::_1);
    fun(1, 100);
    std::cout << b << '\n';
    return 0;
}