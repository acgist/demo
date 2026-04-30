#include <ratio>
#include <iostream>

int main() {
    using v = std::ratio<1, 10>;
    using x = std::ratio<1, 10>;
    // using z = std::ratio_add<v, x>;
    using r = std::ratio_less<v, x>;
    std::cout << r::value << '\n';
    return 0;
}