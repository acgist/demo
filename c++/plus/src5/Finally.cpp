#include <iostream>
#include <functional>

class Finally {

public:
    std::function<void(void)> f;

public:
    Finally(std::function<void(void)> f) : f(f) {
    };
    ~Finally() {
        f();
    }

};

int main() {
    auto x = Finally([]() {
        std::cout << "2\n";
    });
    std::function<void(void)> f{[](){
        std::cout << "3\n";
    }};
    std::cout << "1\n";
    return 0;
}