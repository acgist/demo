#include <string>
#include <iostream>
#include <string_view>

int main() {
    std::string s = "1234";
    // std::string m = s;
    std::string m = std::move(s);
    // std::string&& m { std::move(s) };
    // const std::string&& m = std::move(s);
    std::string_view v = s;
    std::cout << s << '\n';
    s = "xxxx";
    std::cout << s << '\n';
    std::cout << m << '\n';
    std::cout << v << '\n';
    std::cout << &s << '\n';
    std::cout << &m << '\n';
    std::cout << &v << '\n';
    return 0;
}