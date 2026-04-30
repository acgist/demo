#include <iostream>

int main() {
    int* a = new int[4] { 1, 2, 3, 4 };
    int* b = new int[4] { 4, 4, 4, 4 };
    std::cout << (long long) a << '\n';
    void* c = memcpy(a, b, 2);
    std::cout << (long long) c << '\n';
    delete a;
    delete b;
    a = nullptr;
    b = nullptr;
    return 0;
}