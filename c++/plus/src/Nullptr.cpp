#include <iostream>

int main() {
    int* num = nullptr;
    if(!num) {
        num = new int(0);
    }
    delete num;
    num = nullptr;
    if(num) {
        std::cout << "exist = " << *num;
    } else {
        std::cout << "none";
    }
    return 0;
}