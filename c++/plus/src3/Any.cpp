#include <any>
#include <string>
#include <iostream>

int main() {
    std::any a = 1;
    a = 'd';
    a.emplace<char>('x');
    // std::cout << a << '\n';
    std::cout << a.has_value() << '\n';
    std::cout << a.type().name() << '\n';
    a.reset();
    std::any b = "1234";
    std::cout << b.type().name() << '\n';
    std::any c{ std::in_place_type<std::string>, "1234" };
    std::cout << c.type().name() << '\n';
    // std::string cv = std::any_cast<std::string>(c);
    std::string cv = std::any_cast<std::string&>(c);
    std::cout << cv << '\n';
    return 0;
}