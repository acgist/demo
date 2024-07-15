#include <tuple>
#include <iostream>

int main() {
    std::tuple<int, int, int> a{ 1, 2, 3 };
    std::cout << sizeof(a) << '\n';
    std::cout << std::get<0>(a) << '\n';
    std::cout << std::get<1>(a) << '\n';
    std::cout << "====1\n";
    int aa = 0;
    int bb = 0;
    std::tie(aa, bb, std::ignore) = a;
    std::cout << aa << '\n';
    std::cout << bb << '\n';
    std::cout << "====2\n";
    auto& [ar, br, cr] = a;
    std::cout << ar << '\n';
    std::cout << br << '\n';
    std::cout << cr << '\n';
    std::cout << "====3\n";
    std::apply([](int a1, int b1, int c1) {
        std::cout << a1 << '\n';
        std::cout << b1 << '\n';
        std::cout << c1 << '\n';
    }, a);
    return 0;
}