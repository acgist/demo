#include <map>
#include <string>
#include <iostream>

int main() {
    const std::string a = "1234";
    std::string b = std::move(a);
    std::cout << a << '\n';
    std::cout << b << '\n';
    const std::map<std::string, std::string> map {
        { "1", "1" },
        { "2", "2" }
    };
    std::map<std::string, std::string> cpy;
    for(auto& [k, v] : map) {
        cpy.emplace(std::move(k), std::move(v));
    }
    for(const auto& [k, v] : map) {
        std::cout << k << " = " << v << '\n';
    }
    for(const auto& [k, v] : cpy) {
        std::cout << k << " = " << v << '\n';
    }
    return 0;
}