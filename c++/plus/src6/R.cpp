#include <iostream>

template<unsigned char f>
class X {
public:
    static const unsigned long long value = f * X<f - 1>::value;
};

template<>
class X<0> {
public:
    static const unsigned long long value = 1;
};

inline constexpr unsigned long long x(unsigned char f) {
    if(f == 0) {
        return 1;
    }
    return f * x(f - 1);
}

int main() {
    std::cout << X<4>::value << '\n';
    std::cout << x(4) << '\n';
    return 0;
}