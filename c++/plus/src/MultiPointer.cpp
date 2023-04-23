#include <iostream>

int main(int argc, char const *argv[]) {
    int a = 0;
    int* aPointer = &a;
    int** aPointerPointer = &aPointer;
    std::cout << a << std::endl;
    std::cout << *aPointer << std::endl;
    std::cout << **aPointerPointer << std::endl;
    return 0;
}
