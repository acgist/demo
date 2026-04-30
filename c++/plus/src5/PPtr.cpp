#include <iostream>

int** iptr() {
    int* a = new int{100};
    std::cout << a << '\n';
    int** a1 = &a;
    std::cout << a1 << "====" << **a1 << "====" << *a1 << '\n';
    int** a2 = &a;
    std::cout << a2 << "====" << **a2 << "====" << *a2 << '\n';
    return a2;
}

int main() {
    int** a = iptr();
    std::cout << a << "====" << **a << "====" << *a << '\n';
    return 0;
}