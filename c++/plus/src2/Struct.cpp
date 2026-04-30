#include <vector>
#include <iostream>

struct P {

int age = 0;
int name() {
    return 0;
};

};

class PP {

public:
    int* i = nullptr;

public:
    PP() {
        this->i = new int{100};
        throw "1234";
    }
    ~PP() {
        std::cout << "~PP\n";
    }

};

int main() {
    P p;
    P* ptr = new P{};
    std::cout << p.age << p.name() << "\n";
    std::cout << ptr->age << ptr->name() << "\n";
    std::vector<int> a(4);
    std::vector<int> b{4};
    std::cout << a.size() << "\n";
    std::cout << b.size() << "\n";
    PP* pp = nullptr;
    try {
        pp = new PP{};
    } catch(const char * e) {
        std::cout << "exception pp->i" << pp << "\n";
        delete pp;
        std::cerr << e << '\n';
    }
    std::cout << "non-exception pp->i" << pp << "\n";
    delete pp;
    return 0;
}