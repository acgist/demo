#include <vector>
#include <iostream>

class P {

};

class M : public P {

};

void fun(std::vector<P> v) {
    std::cout << "impl\n";
}

template<typename T>
void fun(std::vector<T> v) {
    std::cout << "template\n";
}

int main() {
    std::vector<M> v(10);
    std::cout << v.size() << "\n";
    // fun(v);
    fun(v);
    return 0;
}
