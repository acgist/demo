#include <iostream>

int main(int argc, char const *argv[]) {
    uint8_t a = 3;
    uint8_t b = 2;
    float c = ((a << 8) & 0xFF00) | (b & 0x00FF);
    std::cout << (int) a << std::endl;
    std::cout << (int) b << std::endl;
    std::cout << c << std::endl;
    a = (uint8_t) ((((uint16_t) c) >> 8) & 0xFF);
    b = (uint8_t) (((uint16_t) c) & 0xFF);
    std::cout << (int) a << std::endl;
    std::cout << (int) b << std::endl;
    return 0;
}