#include <iostream>

void setInt(void** number) {
    // int a = 10;
    static int a = 10;
    *number = &a;
    // *number = &a;
    // **number = 10;
}

void setIntEx(int** number) {
    **number = 10;
}

void setIntExx(int* number) {
    setIntEx(&number);
}

int main(int argc, char const *argv[]) {
    int* p = nullptr;
    std::cout << p << std::endl;
    // setInt((void**)&p);
    // setIntEx(&p);
    setIntExx(p);
    std::cout << p << std::endl;
    std::cout << *p << std::endl;
    std::cout << *p << std::endl;
    return 0;
}
