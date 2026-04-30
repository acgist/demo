#include <bitset>
#include <iostream>

int main(int argc, char const *argv[]) {
    unsigned char v = 1;
    // signed char v = 1;
    std::cout << (int) v << std::endl;
    v = 250;
    std::cout << (int) v << std::endl;
    char x = u'测';
    std::cout << x << std::endl;
    int a = 10;
    int b = 010;
    int c = 0X10;
    int d = 0B10;
    int e = 1'100'100;
    std::cout << a << std::endl;
    std::cout << b << std::endl;
    std::cout << c << std::endl;
    std::cout << d << std::endl;
    std::cout << ~e << std::endl;
    return 0;
}
