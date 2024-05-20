#include <thread>
#include <chrono>
#include <iostream>

int main() {
    int* a = new int(1);
    std::thread thread([&a]() {
        std::cout << (long long) a << " - 1 = " << *a << "\n";
    });
    thread.detach();
    std::cout << (long long) a << " - 2 = " << *a << "\n";
    std::this_thread::sleep_for(std::chrono::seconds(5));
    return 0;
}