#include <bit>
#include <iostream>

int main() {
    int* array = new int[1];
    array[0] = 1;
    char* cArray = (char*) array;
    std::cout << (long long) array  << '\n';
    std::cout << (long long) cArray << '\n';
    char* xArray = reinterpret_cast<char*>(array);
    std::cout << (long long) xArray << '\n';
    char* zArray = std::bit_cast<char*>(array);
    std::cout << (long long) zArray << '\n';
    return 0;
}