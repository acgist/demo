#include <iostream>

int main() {
    if(nullptr) {
        std::cout << "nullptr = true" << std::endl;
    } else {
        std::cout << "nullptr = false" << std::endl;
    }
    if(0) {
        std::cout << "0 = true" << std::endl;
    } else {
        std::cout << "0 = false" << std::endl;
    }
    if(-1) {
        std::cout << "-1 = true" << std::endl;
    } else {
        std::cout << "-1 = false" << std::endl;
    }
    if(1) {
        std::cout << "1 = true" << std::endl;
    } else {
        std::cout << "1 = false" << std::endl;
    }
    if('0') {
        std::cout << "'0' = true" << std::endl;
    } else {
        std::cout << "'0' = false" << std::endl;
    }
    if('\0') {
        std::cout << "'a' = true" << std::endl;
    } else {
        std::cout << "'a' = false" << std::endl;
    }
    if('1') {
        std::cout << "'1' = true" << std::endl;
    } else {
        std::cout << "'1' = false" << std::endl;
    }
    return 0;
}