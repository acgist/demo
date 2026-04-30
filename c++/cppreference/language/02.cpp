#include <string>
#include <numeric>
#include <iostream>

void g(int& i) {
    std::cout << i << "&i\n";
}

void g(const int& i) {
    std::cout << i << "c&i\n";
}

void g(int&& i) {
    std::cout << i << "&&i\n";
}

template<typename T>
void fun(T&& t) {
    g(std::forward<T>(t));
}

// std::string s() {
//     std::string v = "1234";
//     return v;
// }

// std::string& s() {
//     std::string v = "1234";
//     return v;
// }

union Number {
    long long d;
    char      a;
    short     b;
    int       c;
};

int main() {
    int i = 200;
    const int x = 300;
    g(i);
    g(x);
    g(100);
    fun(i);
    fun(x);
    fun(100);
    // std::string v = s();
    // std::string& v = s(); // 悬垂引用
    // std::string&& v = s();
    // const std::string& v = s();
    // std::cout << v << '\n';
    int a[10] { 0 };
    for(const auto& v : a) {
        std::cout << v << ' ';
    }
    std::cout << std::endl;
    std::iota(std::begin(a), std::end(a), 10);
    for(const auto& v : a) {
        std::cout << v << ' ';
    }
    Number number = { 1234567890L };
    std::cout << "\n=============\n";
    std::cout << number.a << '\n';
    std::cout << number.b << '\n';
    std::cout << number.c << '\n';
    std::cout << number.d << '\n';
    return 0;
}