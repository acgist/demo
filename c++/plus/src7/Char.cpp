#include <iostream>

int main() {
    char a = '0';
    char b = '\0';
    char c = 0;
    std::cout << c << '\n';
    if(a) {
        std::cout << "a\n";
    }
    if(b) {
        std::cout << "b\n";
    }
    if(c) {
        std::cout << "c\n";
    }
    std::cout << (int) b << '\n';
    std::cout << (b == c) << '\n';
    return 0;
}