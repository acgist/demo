#include <iostream>

int add(const int& a, const int& b) {
    return a + b;
};

int* mult(const int& a, const int& b) {
    int* c = new int(a * b);
    return c;
} 

int main(int argc, char const *argv[]) {
    // 如果没有括号表示定义一个返回int*的函数
    int (*addPtr)(const int&, const int&) = add;
    // int (*addPtr)(const int& a, const int& b) = add;
    // int (*addPtr)(const int& a, const int& b) = &add;
    // int c = addPtr(1, 2);
    int c = (*addPtr)(1, 2);
    std::cout << c << std::endl;
    int* cPtr = mult(1, 2);
    std::cout << *cPtr << std::endl;
    delete cPtr;
    std::cout << *cPtr << std::endl;
    std::cout << *cPtr << std::endl;
    std::cout << *cPtr << std::endl;
    return 0;
}
