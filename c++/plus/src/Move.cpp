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
    std::cout << *m.m   << " - " << m.m   << "\n";
    std::cout << *mm.m  << " - " << mm.m  << "\n";
    std::cout << *mmm.m << " - " << mmm.m << "\n";
    return 0;
}