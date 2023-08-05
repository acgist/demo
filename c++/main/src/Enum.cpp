#include <iostream>

enum Number {

    ONE = 1,
    TWO

};

enum Char {

    A = 'a',
    B = 'b',
    C,
    // 注意：默认+1=d
    F

};

using integer = int;

int v(void) {
    return 0;
}

int main(void) {
    std::cout << Number::ONE << std::endl;
    std::cout << (char) Char::A << std::endl;
    std::cout << (char) Char::C << std::endl;
    std::cout << (char) Char::F << std::endl;
    integer a = 1;
    return 0;
}