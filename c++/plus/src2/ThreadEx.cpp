#include <thread>
#include <chrono>
#include <iostream>

int* x = nullptr;

int main() {
    std::thread t([]() {
        int* v = new int(10);
        x = v;
        std::cout << "v = " << *v << "\n";
        std::cout << "x = " << *x << "\n";
    });
    t.detach();
    std::this_thread::sleep_until(std::chrono::system_clock::now() + std::chrono::seconds(5));
    std::cout << (x == nullptr) << "\n";
    std::cout << "x = " << *x << "\n";
    return 0;
}