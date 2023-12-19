#include <iostream>
#include <typeinfo>

template<typename T>
void testTemplate(T& t) {
    t + 1;
}

class Template {
public:
    int num;
    void operator+(int value) {
        this->num += value;
    }
};

template<typename T>
auto backTemplate(const T& a, const T& b) -> long {
// auto backTemplate(const T& a, const T& b) -> decltype(a + b) {
    return a + b;
}


int main(int argc, char const *argv[]) {
    // double& x = 1;
    // double&& x = 2;
    Template t;
    t.num = 1;
    std::cout << t.num << std::endl;
    testTemplate(t);
    std::cout << t.num << std::endl;
    // auto x = backTemplate(1, 2);
    auto x = backTemplate<int>(1, 2L);
    const std::type_info& type = typeid(x);
    std::cout << x << " = " << type.name() << std::endl;
    return 0;
}
