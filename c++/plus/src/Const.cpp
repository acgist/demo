#include <iostream>

int main() {
    int a = 100;
    const int& aRef = a;
    // 理论上下面的写法无效
    // int& const aRef = a;
    // aRef = 2;
    // std::cout << a    << std::endl;
    // std::cout << aRef << std::endl;
    return 0;
}