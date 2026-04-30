#include <string>
#include <iostream>

int main() {
    std::string str;
    while(true) {
        std::getline(std::cin, str, '#');
        // std::cin >> str;
        // std::cin.peek()
        // std::cin.getline()
        std::cout << str << "\n";
    }
    return 0;
}