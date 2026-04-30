#include <string>
#include <cstring>
#include <cstdlib>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::string* array = new std::string[11];
    std::cout << "length = " << (sizeof(array) / sizeof(array[0])) << std::endl;
    for (int index = 0; index < 11; index++) {
        array[index] = std::to_string(index) + " = " + std::to_string(std::rand());
    }
    std::cout << "length = " << (sizeof(array) / sizeof(array[0])) << std::endl;
    for (int index = 0; index < 11; index++) {
        std::cout << "i = " << array[index] << std::endl;
    }
    for (int index = 0; index < 11; index++) {
        std::cout << "p = " << *array << std::endl;
        array++;
    }
    std::cout << "length = " << (sizeof(array) / sizeof(array[0])) << std::endl;
    return 0;
}
