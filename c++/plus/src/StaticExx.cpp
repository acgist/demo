#include <vector>
#include <iostream>

static int a = 100;
static int b = 1000;

static std::vector<int> v1;

int main() {
    std::cout << &a << std::endl;
    a = 1000;
    std::cout << &a << std::endl;
    a = b;
    std::cout << &a << std::endl;
    std::cout << &v1 << std::endl;
    std::cout << v1.size() << std::endl;
    v1 = { 1, 2, 3 };
    std::cout << &v1 << std::endl;
    std::cout << v1.size() << std::endl;
    return 0;
}