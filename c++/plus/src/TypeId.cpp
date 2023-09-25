#include <iostream>
#include <typeinfo>

int main(int argc, char const *argv[]) {
    int a = 0;
    std::cout << sizeof(a) << std::endl;
    const std::type_info& info = typeid(a);
    std::cout << info.name() << std::endl;
    return 0;
}
