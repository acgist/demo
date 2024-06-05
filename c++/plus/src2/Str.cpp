#include <string>
#include <cstring>
#include <iostream>

int main() {
    std::string s(20, 'a');
    std::cout << s << "\n";
    s[1] = 'b';
    std::cout << s << "\n";
    char ss[10] { 0 };
    // char* ss = new char[10] { 0 };
    std::cout << sizeof(ss) << "\n";
    std::cout << sizeof(ss) / sizeof(char) << "\n";
    std::cout << std::strlen(ss) << "\n";
    std::string copy(&s[0], &s[3]);
    std::cout << copy << '\n';
    return 0;
}
