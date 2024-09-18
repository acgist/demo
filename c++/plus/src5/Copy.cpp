#include <memory>
#include <iostream>

int main() {
    char  a[4] { 1, 2, 3, 4 };
    float b[4];
    // short b[4];
    // memcpy(b, a, 4);
    std::copy(a, a + 4, b);
    for(const auto& v : a) {
        std::cout << static_cast<int>(v) << " = a\n";
    }
    for(const auto& v : b) {
        std::cout << v << " = b\n";
    }
    return 0;
}