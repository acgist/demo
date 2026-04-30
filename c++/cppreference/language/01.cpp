#include <cstddef>
#include <iostream>

constexpr int operator""_i(char i) {
    return i * 2;
}

int main() {
    int* a;
    int* b = nullptr;
    std::nullptr_t c;
    std::cout << (a == c) << '\n';
    std::cout << (b == c) << '\n';
    for(char i = ' '; i <= '~'; ++i) {
        std::cout << i << ((i + 1) % 32 ? ' ' : '\n');
    }
    std::cout << ('2'_i) << '\n';
    int n = 1000;
    int&& m = std::move(n);
    std::cout << n << '=' << m << '\n';
    return 0;
}