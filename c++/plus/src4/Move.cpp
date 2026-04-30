#include <memory>
#include <utility>
#include <iostream>

class P {

public:
    int* a = nullptr;
    int  b = 0;
    std::unique_ptr<int> c{ nullptr };
    P() = default;
    // P(P&& p) = default;
    P(P&& p) {
        this->a = std::exchange(p.a, nullptr);
        this->b = std::exchange(p.b, 0);
        this->c = std::exchange(p.c, nullptr);
        // std::swap(*this, p);
    }

};

int main() {
    P p1;
    p1.a = new int{100};
    p1.b = 1;
    p1.c = std::unique_ptr<int>(new int{10});
    // P&& p2 = std::move(p1);
    P p2{std::move(p1)};
    if(p1.a) {
        std::cout << *p1.a << '\n';
    }
    std::cout << p1.b << '\n';
    if(p1.c) {
        std::cout << *p1.c << '\n';
    }
    std::cout << "====\n";
    if(p2.a) {
        std::cout << *p2.a << '\n';
    }
    std::cout << p2.b << '\n';
    if(p2.c) {
        std::cout << *p2.c << '\n';
    }
    std::string name1 = "1234";
    std::string name2 = std::move(name1);
    std::cout << "name1 = " << name1 << '\n';
    std::cout << "name2 = " << name2 << '\n';
    return 0;
}