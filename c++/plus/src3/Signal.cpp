#include <iostream>

class Instance {

private:
    Instance() {};
public:
    Instance(const Instance& instance) = delete;
    Instance operator=(const Instance& instance) = delete;
    static Instance& getInstance() {
        // C++11保证局部静态变量线程安全
        static Instance instance;
        return instance;
    }

};

int main() {
    auto& a = Instance::getInstance();
    auto& b = Instance::getInstance();
    // auto c = b;
    // auto c = Instance::getInstance();
    std::cout << &a << "\n";
    std::cout << &b << "\n";
    return 0;
}
