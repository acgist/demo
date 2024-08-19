#include <thread>
#include <chrono>
#include <iostream>

int main() {
    std::thread* t = new std::thread([&]() {
        while(true) {
            std::cout << t << " = 1\n";
            std::this_thread::sleep_for(std::chrono::milliseconds(500));
        }
    });
    t->detach();
    std::this_thread::sleep_for(std::chrono::milliseconds(1000));
    delete t;
    t = nullptr;
    std::cout << "~t\n";
    std::this_thread::sleep_for(std::chrono::seconds(60));
    return 0;
}