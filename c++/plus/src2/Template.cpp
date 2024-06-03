#include <iostream>

class A {
public:
    template <typename T>
    T function_m() {
        T t = '*';
        return t;
    }
};

template <typename U>
void function_n(U argument) {
    // char ret = argument.function_m<char>();
    char ret = argument.template function_m<char>();
    std::cout << "function_n" << ret << "\n";
}

void function_x(A a) {
    char ret = a.function_m<char>();
    std::cout << "function_x" << ret << "\n";
}

int main() {
    A a;
    a.function_m<char>();
    function_n(a);
    function_x(a);
    return 0;
}