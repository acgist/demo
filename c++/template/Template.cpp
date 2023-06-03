#include <iostream>

// template<class T>
template<typename T>
T plus(T a, T b) {
    return a + b;
}

template<typename T>
void swap(T& a, T& b) {
    const T tmp = a;
    a = b;
    b = tmp;
}

template<class T>
void unknow() {
    std::cout << "1234" << std::endl;
}

template<class T>
void sort(T array[], int length) {
    for(int i = 0; i < length - 1; i++) {
        for(int j = 0; j < length - i - 1; j++) {
            T a = array[j];
            T z = array[j + 1];
            if(a > z) {
                array[j] = z;
                array[j + 1] = a;
            }
        }
    }
}

template<class T>
void arrayPrint(T array[], int length) {
    for(int i = 0; i < length; i++) {
        std::cout << array[i] << " - ";
    }
    std::cout << std::endl;
}

int main(int argc, char const *argv[]) {
    // int a = 1;
    // int b = 2;
    // std::cout << plus(a, b) << std::endl;
    // std::cout << a << " - " << b << std::endl;
    // swap(a, b);
    // std::cout << a << " - " << b << std::endl;
    // double aa = 1;
    // double bb = 2;
    // std::cout << plus(aa, bb) << std::endl;
    // std::cout << aa << " - " << bb << std::endl;
    // swap(aa, bb);
    // std::cout << aa << " - " << bb << std::endl;
    // // 自动类型推导
    // swap(aa, bb);
    // // 显示指定类型
    // swap<double>(aa, bb);
    // unknow<int>();
    // int array[] = { 2, 1, 3, 4 };
    char array[] = "123456";
    int length = sizeof(array) / sizeof(array[0]);
    arrayPrint(array, length);
    sort(array, length);
    arrayPrint(array, length);
    return 0;
}
