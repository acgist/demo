#include <string>
#include <iostream>

class P {

public:
    std::string a;
    P(int a) : P(std::to_string(a)) {
        // P(std::to_string(a));
        // this->a = std::to_string(a);
        // *this = P(std::to_string(a));
    }
    P(const std::string& a) {
        this->a = a;
    }
    ~P() {
        std::cout << "~P\n";
    }

};

int main() {
    P p{1};
    std::cout << "p.a = " << p.a << '\n';
    return 0;
}