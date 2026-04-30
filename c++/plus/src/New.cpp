#include <new>
#include <iostream>

int main(int argc, char const *argv[]) {
    int* a = new int[40];
    int* b = new (a) int[10];
    a[0] = 101;
    b[0] = 100;
    std::cout << a[0] << std::endl;
    std::cout << a[1] << std::endl;
    // delete[] b;
    delete[] a;
    std::cout << "----" << std::endl;
    return 0;
}
