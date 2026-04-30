#include <string>
#include <iostream>

int main() {
    std::string x = "1234";
    std::cout << x.size() << '\n';
    std::cout << x.length() << '\n';
    std::cout << x.data() << '\n';
    std::cout << x.c_str() << '\n';
    return 0;
}