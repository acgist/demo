#include <iostream>

class P {

public:
    void* operator new(size_t size) {
        std::cout << size << " = new P\n";
        return ::operator new(size);
    }
    void operator delete(void* ptr) {
        std::cout << "delete P\n";
        ::operator delete(ptr);
    }

};

int main() {
    P* p = new P{};
    delete p;
    return 0;
}