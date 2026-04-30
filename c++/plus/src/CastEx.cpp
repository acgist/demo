#include <iostream>

class Const {

public:
    double d = 0;
    int x = 10;
    // const int x = 10;
    enum  {   v = 100 };
    
    Const() {
    }
    
    Const(const double& d) {
        this->d = d;
        std::cout << "===1\n";
    }

    Const& operator= (const Const& source) {
        this->d = source.d;
        std::cout << "===2\n";
        return *this;
    }

    operator double() const {
        return this->d;
    }

};

int main() {
    double v = 1.1;
    int    x = v;
    std::cout << v << '\n';
    std::cout << x << '\n';
    Const c;
    std::cout << c.x << '\n';
    std::cout << c.v << '\n';
    Const d = 1.0;
    std::cout << d.d << '\n';
    std::cout << &d << '\n';
    d = 2.0;
    std::cout << d.d << '\n';
    std::cout << &d << '\n';
    double ret = (double) d;
    std::cout << ret << '\n';
    return 0;
}