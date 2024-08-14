#include <string>
#include <iostream>

int main() {
    std::string content = "1\n2\n3";
    size_t pos = content.find('\n');
    while(pos >= 0 && pos != std::string::npos) {
        std::cout << "++++" << content.substr(0, pos + 1) << '\n';
        content = content.substr(pos + 1);
        std::cout << "====" << content << '\n';
        pos     = content.find('\n') ;
    }
    return 0;
}