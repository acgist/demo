#include <iostream>

// #pragma pack(1)

struct A {
    int a;
    char b;
    short c;
};

struct  B {
    char b;
    int a;
    short c;
};

struct  C {
    char b;
    short c;
    int a;
};

int main() {
    std::cout << sizeof(A) << "\n";
    std::cout << sizeof(B) << "\n";
    std::cout << sizeof(C) << "\n";
    std::cout << sizeof(int) << "\n";
    std::cout << sizeof(long) << "\n";
    int* x = new int{100};
    long* xx = (long*) x;
    std::cout << *xx << "\n";
    long long* xxx = (long long*) x;
    std::cout << *xxx << "\n";
    return 0;
}