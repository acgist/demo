#include <string>
#include <iostream>

int main() {
    std::string name = "1.2.2.3.exe";
    auto pos = name.find_last_of('-');
    if(pos == std::string::npos) {
        std::cout << "????\n";
    } else {
        std::cout << name.substr(pos) << '\n';
    }
    return 0;
}