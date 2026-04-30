#include <string>
#include <iostream>

void reff(const std::string& value) {
    std::cout << "reff 1 = " << value << '\n';
}

void reff(const std::string&& value) {
    std::cout << "reff 2 = " << value << '\n';
}

int main() {
    std::string a = "a";
    std::string b = "b";
    reff("raw");
    reff(a);
    reff(std::move(a));
    reff(a + b);
    return 0;
}