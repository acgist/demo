#include <vector>
#include <iostream>

class Int {

public:
    int i;

public:
    Int(int v) {
        this->i = v;
    }
    Int(const Int& v) {
        this->i = v.i;
    }
    Int(Int&& v) {
        this->i = v.i;
        v.i = 0;
    }
    Int& operator=(const Int& v) {
        this->i = v.i;
        return *this;
    }
    Int& operator=(Int&& v) {
        this->i = v.i;
        v.i = 0;
        return *this;
    }

};

int main() {
    std::vector<Int> a{1, 2, 3, 4};
    std::vector<Int> b;
    b.assign(std::move_iterator(a.begin()), std::move_iterator(a.begin() + 2));
    // a.erase(a.begin(), a.begin() + 2);
    for(const auto& x : a) {
        std::cout << x.i << std::endl;
    }
    std::cout << "====\n";
    for(const auto& x : b) {
        std::cout << x.i << std::endl;
    }
    return 0;
}