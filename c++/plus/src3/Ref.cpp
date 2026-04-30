#include <iostream>

void swap(int& a, int& b) {
    int c = a;
    a = b;
    b = c;
}

int main() {
    int a{0};
    int b{1};
    int& aRef{a};
    int& bRef{b};
    aRef = b;
    std::cout << &a    << '\n';
    std::cout << &b    << '\n';
    std::cout << &aRef << '\n';
    std::cout << &bRef << '\n';
    aRef = 100;
    std::cout << a    << '\n';
    std::cout << b    << '\n';
    std::cout << aRef << '\n';
    std::cout << bRef << '\n';
    // swap(a, b);
    // int* ap = &aRef;
    // int* bp = &bRef;
    // swap(*ap, *bp);
    swap(aRef, bRef);
    std::cout << "====\n";
    std::cout << a << '\n';
    std::cout << b << '\n';
    return 0;
}