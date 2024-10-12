#include <any>
#include <iostream>

int main() {
    std::any a(1);
    // std::make_any
    std::cout << a.type().name() << '\n';
    // std::cout << a.type().raw_name() << '\n';
    a = "1234";
    std::cout << a.type().name() << '\n';
    // std::cout << a.type().raw_name() << '\n';
    const char* b = std::any_cast<const char*>(a);
    std::cout << b << '\n';
    return 0;
}