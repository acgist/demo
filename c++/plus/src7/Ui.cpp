#include <iostream>

int main() {
    int a;
    int b{};
    std::cout << a << '\n';
    std::cout << b << '\n';
    // int a = { 1.0 }; // narrowing: 窄化
    return 0;
}
