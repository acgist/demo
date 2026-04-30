#include <string>
#include <iostream>

const char* name() {
    std::string path = "测试";
    return path.c_str();
    // const char* path = "测试";
    // return path;
}

const char* refName() {
    const char* path = name();
    std::cout << "refName = " << path << "\r\n";
    std::cout << "refName = " << path << "\r\n";
    return path;
}

void print() {
    const char* path = refName();
    std::cout << "print = " << path << "\r\n";
    std::cout << "print = " << path << "\r\n";
}

int main() {
    print();
    return 0;
}