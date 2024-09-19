#include <string>
#include <iostream>

std::string get() {
    std::string v = "1234";
    std::cout << "== " << &v << '\n';
    return v;
}

int main() {
    std::string x = get();
    std::cout << &x << '\n';
    auto&& t = get();
    std::cout << &t << '\n';
    return 0;
}