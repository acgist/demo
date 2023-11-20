#include <iostream>

int* buildArray();
void arrayLength(int* array);

int main(int argc, char const *argv[]) {
    int* array = buildArray();
    // int* array = (int*) buildArray();
    for(int index = 0; index < 3; ++index) {
        std::cout << array[index] << std::endl;
    }
    arrayLength(array);
    delete array;
    return 0;
}

int* buildArray() {
// long long buildArray() {
    // return (long long) array;
    int* array = new int[3] { 1, 2, 3 };
    std::cout << "length = " << (sizeof(array) / sizeof(int)) << std::endl;
    return array;
}

void arrayLength(int* array) {
    std::cout << "length = " << (sizeof(array) / sizeof(array[0])) << std::endl;
}