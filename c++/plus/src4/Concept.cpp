#include <concepts>
#include <iostream>

class P {

};

template<typename T>
concept EP = std::derived_from<T, P>;

template<EP X>
// template<std::derived_from<P> X>
// template<typename X> requires std::derived_from<X, P>
class M {

// static_assert(std::is_base_of_v<P, X>, "继承类型错误");

public:
    void say(const X& x) const {
        std::cout << &x << '\n';
    }

};

int main() {
    M<P> m1;
    P p;
    m1.say(p);
    // M<const char*> m2;
    // m2.say("");
    return 0;
}