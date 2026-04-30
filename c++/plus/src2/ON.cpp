#include <iostream>

class P {

public:
    static void* operator new(size_t size) {
        std::cout << "new = " << size << "\n";
        // return operator new(sizeof(P));
        return malloc(sizeof(P));
    }
    static void operator delete(void* p) {
        std::cout << "delete\n";
    }
    static void* operator new[](size_t size) {
        std::cout << "new[] = " << size << "\n";
        // return operator new(sizeof(P));
        return malloc(size * sizeof(P));
    }
    static void operator delete[](void* p) {
        std::cout << "delete[]\n";
    }

};

int main() {
    P* p = new P{};
    delete p;
    P* pp = new P[10];
    delete[] pp;
}
