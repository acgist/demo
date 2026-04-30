#include <iostream>

int main() {
    int a = 10;
    const auto  ap1 = &a;
    const auto* ap2 = &a;
    std::cout << ap1 << '\n';
    std::cout << ap2 << '\n';
    return 0;
}