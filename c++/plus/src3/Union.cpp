#include <bitset>
#include <iostream>

union A {

public:
    int   a;
    char  c;
    short b;

};

int main() {
    A a;
    a.a = 0B1000'0000'0000'0000'1000'0000'0000'1000;
    // 小端
    std::cout << std::bitset<sizeof(a.a) * 8>(a.a) << "\n";
    std::cout << std::bitset<sizeof(a.b) * 8>(a.b) << "\n";
    std::cout << std::bitset<sizeof(a.c) * 8>(a.c) << "\n";
}
