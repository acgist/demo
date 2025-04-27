#include <iostream>

class S1 {
public:
    virtual ~S1(){};
};
class S2 {};

class E1 : public S1 {};
class E2 : public S2 {};

int main() {
    E1 e1;
    E2 e2;
    S1& s1 = e1;
    S2& s2 = e2;
    std::cout
    << "e1 = " << typeid(decltype(e1)).name() << '\n'
    << "e2 = " << typeid(decltype(e2)).name() << '\n'
    << "s1 = " << typeid(decltype(s1)).name() << '\n'
    << "s2 = " << typeid(decltype(s2)).name() << '\n'
    << "e1 = " << typeid((e1)).name() << '\n'
    << "e2 = " << typeid((e2)).name() << '\n'
    << "s1 = " << typeid((s1)).name() << '\n'
    << "s2 = " << typeid((s2)).name() << '\n';
    return 0;
}