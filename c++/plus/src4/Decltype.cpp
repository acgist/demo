#include <iostream>

const int& getInt() {
    int* a = new int{0};
    std::cout << a << '\n';
    return *a;
}

template<typename T, typename X>
auto get(T t, X x) {
// decltype(t + x) get(T t, X x) {
// auto get(T t, X x) -> decltype(t + x) {
    return t + x;
}

int main() {
    auto a = getInt();
    std::cout << &a << "\n";
    decltype(getInt()) b = getInt();
    std::cout << &b << "\n";
    decltype(auto) c = getInt();
    std::cout << &c << "\n";
    std::cout << get(1, 2.1) << '\n';
    return 0;
}