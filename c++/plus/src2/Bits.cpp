#include <cstdint>
#include <iostream>

int main() {
    int a = 1234;
    char*  ac = (char*)  &a;
    short* sc = (short*) &a;
    std::cout << (int) ac[0] << "\n";
    std::cout << (int) ac[1] << "\n";
    std::cout << (int) ac[2] << "\n";
    std::cout << (int) ac[3] << "\n";
    std::cout << (uint16_t) sc[0] << "\n";
    std::cout << (uint16_t) sc[1] << "\n";
    char c[]{ 4, -46 };
    // char c[]{ -46, 4 };
    short* cs = (short*) c;
    std::cout << *cs << "\n";
}
