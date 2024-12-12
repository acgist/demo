/**
 * https://en.cppreference.com/w/cpp/thread/hardware_destructive_interference_size
 */
#include <new>
#include <thread>
#include <chrono>
#include <iostream>

class Person {

public:
    size_t x{ 0 };
    size_t y{ 0 };
    // alignas(std::hardware_destructive_interference_size) size_t x{ 0 };
    // alignas(std::hardware_destructive_interference_size) size_t y{ 0 };

};

int main() {
    Person* p = new Person;
    auto a = std::chrono::system_clock::now();
    std::thread t1([p]() {
        for (size_t i = 0; i < 100'000'000; i++) {
            ++p->x;
        }
    });
    std::thread t2([p]() {
        for (size_t i = 0; i < 100'000'000; i++) {
            ++p->y;
        }
    });
    t1.join();
    t2.join();
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << '\n';
    return 0;
}