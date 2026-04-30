#include <iostream>

int main(int argc, char **argv) {
    int *p = NULL;
    *p = 100;
    std::cout << p << std::endl;
    std::cout << *p << std::endl;
}
