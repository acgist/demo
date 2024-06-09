#include <windef.h>
#include <iostream>

int main() {
    const char* c = "1234";
    std::cout << (c[4] == 0) << std::endl;
    std::cout << (c[4] == '\0') << std::endl;
    std::cout << (c[4] == 1) << std::endl;
    std::cout << (c[4] == '\1') << std::endl;
    std::cout << (FALSE == false) << std::endl;
    std::cout << (0 == false) << std::endl;
    std::cout << (0 == FALSE) << std::endl;
    std::cout << (0 == NULL) << std::endl;
    std::cout << (0 == nullptr) << std::endl;
    std::cout << (3 | 3 == 2) << std::endl;
    return 0;
}