#include <iostream>

const char* getLine() {
    std::string value;
    std::cin >> value;
    const char* data = value.data();
    return data;
}

int main() {
    const char* line = getLine();
    std::cout << "1 = " << line << '\n';
    std::cout << "2 = " << line << '\n';
    std::cout << "3 = " << line << '\n';
    std::cout << "4 = " << line << '\n';
    return 0;
}