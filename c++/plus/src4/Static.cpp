#include <thread>
#include <iostream>

class P {

public:
    static int x;
    // static int a = 0;
    inline static int b = 0;
    const static int c = 0;
    static int say();
    void set(const int a) = delete;
    void set(const double a);

};

/* static */ int P::x = 0;

/* static */ int P::say() {
    ++P::x;
    return 0;
}

void P::set(const double a) {
}

int main() {
    std::cout << P::say() << "\n";
    std::cout << P::say() << "\n";
    std::thread t1([]() {
        for(int i = 0; i < 10000; ++i) {
            P::say();
        }
    });
    std::thread t2([]() {
        for(int i = 0; i < 10000; ++i) {
            P::say();
        }
    });
    t1.detach();
    t2.detach();
    std::this_thread::sleep_for(std::chrono::seconds(5));
    std::cout << P::x << "\n";
    P p;
    // p.set(1);
    p.set(1.0);
    return 0;
}