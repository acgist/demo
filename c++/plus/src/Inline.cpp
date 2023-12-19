#include <string>
#include <iostream>

// inline必须和函数定义放在一起
inline int plus(int a, int b) {
    return a + b;
}

// const可以使用临时变量
void print(const int& x);

const char* charChange(const char* v) {
    const char* xx = "1234";
    std::cout << &xx << std::endl;
    return xx;
    // std::string vv = "1234";
    // return vv.c_str();
    // 错误
    // const char xx[] = "1234";
    // return xx;
}

const int* intPtr() {
    int* x = new int(100);
    return x;
}

// const std::string* stringPtr() {
//     std::string xx = "123456";
//     return &xx;
// }

int main() {
    int v = plus(1, 2);
    std::cout << v << std::endl;
    print(1);
    print(v);
    print(v + 1);
    print(v += 1);
    int& vRef = v;
    // int& vvRef = 1;
    int&& vvvRef = 1;
    const char* x = "1";
    std::cout << "----" << std::endl;
    std::cout << x << std::endl;
    const char* xx = charChange(x);
    std::cout << x << std::endl;
    std::cout << &xx << std::endl;
    std::cout << xx << std::endl;
    std::cout << xx << std::endl;
    std::cout << xx << std::endl;
    std::cout << xx << std::endl;
    std::cout << "----" << std::endl;
    const int* intPtrx = intPtr();
    std::cout << *intPtrx << std::endl;
    std::cout << *intPtrx << std::endl;
    std::cout << *intPtrx << std::endl;
    delete intPtrx;
    // std::cout << "----" << std::endl;
    // const std::string* stringPtrx = stringPtr();
    // std::cout << *stringPtrx << std::endl;
    // std::cout << *stringPtrx << std::endl;
    // std::cout << *stringPtrx << std::endl;
    return 0;
}

// int plus(int a, int b) {
//     return a + b;
// }

void print(const int& x) {
    std::cout << x << std::endl;
}