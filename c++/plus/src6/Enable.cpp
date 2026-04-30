#include <iostream>

template<typename A, typename B>
std::enable_if_t<std::is_same<A, B>::value, bool> is_same(A a, B b) {
// std::enable_if_t<std::is_same_v<A, B>, bool> is_same(A a, B b) {
    std::cout << "is_same true  " << a << " = " << b << "\n";
    return true;
}

template<typename A, typename B>
std::enable_if_t<!std::is_same<A, B>::value, bool> is_same(A a, B b) {
// std::enable_if_t<std::is_same_v<A, B>, bool> is_same(A a, B b) {
    std::cout << "is_same false " << a << " = " << b << "\n";
    return false;
}

int main() {
    is_same(1, 1);
    is_same(1, 1.0);
    return 0;
}