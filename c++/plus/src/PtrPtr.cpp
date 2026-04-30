#include <iostream>

int main() {
    int* intPtr = nullptr;
    std::cout << (intPtr == nullptr) << "\n";
    intPtr = new int{ 1 };
    std::cout << (intPtr == nullptr) << " = " << *intPtr << "\n";
    delete intPtr;
    std::cout << (intPtr == nullptr) << "\n";
    return 0;
}