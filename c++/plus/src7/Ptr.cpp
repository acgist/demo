#include <cstdint>
#include <iostream>

int main() {
    int32_t* i = new int32_t(1);
    std::intptr_t v = reinterpret_cast<std::intptr_t>(i);
    std::cout << sizeof(int32_t) << '\n';
    std::cout << sizeof(std::intptr_t) << '\n';
    std::cout << sizeof(v) << " = " << i << " = 0x" << std::hex << v << '\n';
    delete i;
    i = nullptr;
    return 0;
}