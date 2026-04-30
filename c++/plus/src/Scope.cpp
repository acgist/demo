#include <iostream>

int main() {
    int a = 0;
    std::cout << a << std::endl;
    if(true) {
        int a = 0;
        std::cout << a << std::endl;
        a = 100;
        std::cout << a << std::endl;
    }
    a = 10;
    std::cout << a << std::endl;
    return 0;
}