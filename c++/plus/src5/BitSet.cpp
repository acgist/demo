#include <bitset>
#include <iostream>

int main() {
    std::bitset<10> bs{ "0010" };
    std::cout << bs << '\n';
    bs.flip(0);
    std::cout << bs << '\n';
    std::cout << bs.to_ulong() << '\n';
    return 0;
}