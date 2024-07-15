#include <iostream>
#include <memory>

class P {
public:
    P() {
        std::cout << "P\n";
    };
    ~P() {
        std::cout << "~P\n";
    };
};

void close(const P* v) {
    std::cout << "close = " << v << '\n';
    delete v;
}

std::unique_ptr<P> uPtr() {
    auto ptr { std::make_unique<P>() };
    return std::move(ptr);
    // return ptr;
}

int main() {
    // auto s = std::make_shared<int>(100);
    // auto a = uPtr();
    // P* i = new P{};
    // std::shared_ptr<P> ia {i};
    // // std::shared_ptr<P> ib {i};
    // std::shared_ptr<P> ic {ia};
    // std::cout << ia.use_count() << '\n';
    // // std::cout << ib.use_count() << '\n';
    // std::cout << ic.use_count() << '\n';
    P* i = new P{};
    // std::shared_ptr<P> x{ i, close };
    // std::shared_ptr<P> x{ i, close };
    // auto x = std::make_shared<P>(i, close);
    std::unique_ptr<P, void(*)(const P*)> x{ i, close };
    // auto x = std::make_unique<P, void(*)(const P*)>(i, close);
    // std::cout << x << '\n';
    return 0;
}