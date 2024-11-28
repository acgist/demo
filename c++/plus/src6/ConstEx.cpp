#include <iostream>

int main() {
    const int* v = new int[10];
    int* x = const_cast<int*>(v);
    x[0] = 1;
    std::cout << x[0];
    std::cout << v[0];
    return 0;
}