#include <string>
#include <fstream>
#include <iostream>

#include <string.h>

int main(int argc, char const *argv[]) {
    std::ifstream file;
    file.open("D:/tmp/c++.md", std::ios::in);
    if(file.is_open()) {
        // 方式一：
        // char buffer[1024] = { 0 };
        // while(file >> buffer) {
        //     std::cout << buffer << std::endl;
        //     std::cout << strlen(buffer) << std::endl;
        //     std::string str(buffer);
        //     std::cout << str.length() << std::endl;
        //     std::cout << str.size() << std::endl;
        // }
        // 方式二：
        // char buffer[1024] = { 0 };
        // while(file.getline(buffer, sizeof(buffer))) {
        //     std::cout << buffer << std::endl;
        // }
        // 方式三：
        // std::string buffer;
        // while(std::getline(file, buffer)) {
        //     std::cout << buffer << std::endl;
        // }
        // 方式四：不推荐
        char buffer;
        while(file.get(buffer)) {
            std::cout << buffer << std::endl;
        }
    } else {
        std::cout << "打开失败" << std::endl;
    }
    file.close();
    return 0;
}
