#include <tuple>
#include <iostream>

class Person {

public:
    int a;
    float b;
    double c;
    const char* d;

public:
    Person(int a, float b, double c, const char* d) : a(a), b(b), c(c), d(d) {
    }

};

static double add(int a, float b, double c, const char* d) {
    return c + b + a;
}

int main () {
    float a = 1.0F;
    using tuple = std::tuple<int, float&, double, const char*>;
    tuple v{1, a, 3, "4"};
    // tuple v{1, std::ref(a), 3, "4"};
    // std::cref(a)
    std::cout << typeid(std::get<0>(v)).name() << '\n';
    std::cout << typeid(std::tuple_element<0, tuple>).name() << '\n';
    std::cout << typeid(std::tuple_element<0, tuple>::type).name() << '\n';
    std::cout << std::tuple_size<tuple>::value << '\n';
    std::cout << std::tuple_size<decltype(v)>::value << '\n';
    std::get<1>(v) += 100;
    std::cout << a << '\n';
    auto& [ aa, bb, cc, dd ] { v };
    int aaa;
    float bbb;
    double ccc;
    const char* ddd;
    std::tie(aaa, bbb, ccc, ddd) = v;
    std::cout << (aa == aaa) << '\n'
              << (bb == bbb) << '\n'
              << (cc == ccc) << '\n'
              << (dd == ddd) << '\n';
    // std::tuple_cat
    Person p = std::make_from_tuple<Person>(v);
    std::cout << p.a << '\n';
    std::cout << p.d << '\n';
    std::cout << std::apply(add, v) << '\n';
    return 0;
}