#include <iostream>
#include <concepts>

template<typename T>
concept HasSay = requires(T t) {
    { t.say() } -> std::same_as<int>;
    // t.say() -> std::same_as<int>;
};

template<HasSay P>
// void sayProxy(P p) {
// void sayProxy(P& p) {
void sayProxy(const P& p) {
    p.say();
}

class M {

public:
    int say() const {
        return 0;
    }

};

class N {

};

int main() {
    M m;
    sayProxy(m);
    // N n;
    // sayProxy(n);
    return 0;
}