#include <string>
#include <cctype>
#include <iostream>

int main() {
    char c;
    int count = 0;
    // std::cin >> c;
    std::cin.get(c);
    while(c != '.') {
        if(c == ' ') {
            ++count;
        }
        std::cout << "当前输入：" << c << "----" << std::endl;
        // std::cin >> c;
        std::cin.get(c);
    }
    std::cout << "空格数量：" << count << std::endl;
    std::cout << ". = " << std::ispunct('.') << std::endl;
    std::cout << "v = " << std::ispunct('v') << std::endl;
    std::cout << "d = " << std::isalpha('d') << std::endl;
    std::cout << "1 = " << std::isalpha('1') << std::endl;
    std::cout << "v = " << std::isalnum('v') << std::endl;
    std::cout << "1 = " << std::isalnum('1') << std::endl;
    std::cout << "测 = " << std::ispunct(u'测') << std::endl;
    std::cout << "。 = " << std::ispunct(u'。') << std::endl;
    return 0;
}