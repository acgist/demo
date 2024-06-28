#include <array>
#include <vector>
#include <iostream>

class P {
public:

// struct P {
    int a{100};
    int b;
    int c;

};

void printP(P& p) {
    std::cout << 'p' << p.a << " = " << p.b << " = " << p.c << '\n';
}

int main() {
    std::vector v1{1};
    std::cout << v1.size() << v1[0] << '\n';
    std::vector<int> v2(2);
    std::cout << v2.size() << v2[0] << '\n';
    std::array<int, 2> v3 { 1, 2 };
    auto [a, b] { v3 };
    std::cout << a << " = " << b << '\n';
    P p1;
    printP(p1);
    P p2{};
    printP(p2);
    P p3{1, 2};
    printP(p3);
    P p4{1, 2, 3};
    printP(p4);
    P p5{
        .a = 1,
        .b = 2,
        .c = 3
    };
    printP(p5);
    return 0;
}