#include <vector>
#include <iostream>
#include <functional>

class P {

public:
    ~P(){
        std::cout << "~P\n";
    }

};

auto v() {
    std::vector<std::reference_wrapper<P>> vector;
    P* p{ new P };
    vector.push_back(std::ref(*p));
    return vector;
}

int main() {
    auto&& vector = v();
    std::cout << "====\n";
    delete &(vector[0].get());
    return 0;
}