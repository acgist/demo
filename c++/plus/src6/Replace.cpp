#include <string>
#include <iostream>
#include <algorithm>

int main() {
    std::string v = "1234567890";
    std::replace_if(v.begin(), v.end(), [](char x) {
        return static_cast<int>(x) % 2 == 0;
    }, 'x');
    std::cout << v << '\n';
    return 0;
}