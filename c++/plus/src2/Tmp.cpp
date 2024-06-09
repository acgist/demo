#include <iostream>
#include <cstdio>

int main() {
    char x[L_tmpnam] = { 0 };
    std::tmpnam(x);
    std::cout << x;
    return 0;
}