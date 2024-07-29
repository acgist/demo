#include <iostream>
#include <typeinfo>

class P {

// public:
//     P() {};
//     virtual ~P() {};

};

int main() {
    P p;
    const auto& info = typeid(p);
    // const std::type_info& info = typeid(p);
    std::cout << info.name() << '\n';
    return 0;
}