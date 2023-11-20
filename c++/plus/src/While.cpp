#include <string>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::string name;
    std::cout << "输入名称：";
    std::cin  >> name;
    while(name != "\0") {
        std::cout << name << std::endl;
        std::cin  >> name;
    }
    return 0;
}
