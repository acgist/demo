#include <string>
#include <iostream>

int main(int argc, char const *argv[]) {
    double num = 0.0;
    std::cin >> num;
    std::cout << num << std::endl;
    int key = 1;
    switch(key) {
        case 1:
        std::cout << 1 << std::endl;
        break;
        default:
        std::cout << 2 << std::endl;
        break;
    }
    return 0;
}
