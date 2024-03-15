#include <string>
#include <iostream>

class M {
public:
    int* m;
};

int main() {
    M m;
    m.m = new int(100);
    M mm(m);
    M mmm(std::move(m));
    std::cout << &m   << " - " << m.m   << " - "  << *m.m   << " - "  << " - " << m.m   << "\n";
    std::cout << &mm  << " - " << mm.m  << " - "  << *mm.m  << " - "  << " - " << mm.m  << "\n";
    std::cout << &mmm << " - " << mmm.m << " - "  << *mmm.m << " - "  << " - " << mmm.m << "\n";
    std::string str  = "123";
    std::string str2 = std::move(str);
    std::cout << str  << "\n";
    std::cout << str2 << "\n";
    return 0;
}