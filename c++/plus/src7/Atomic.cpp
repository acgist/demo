#include <atomic>
#include <chrono>
#include <thread>
#include <iostream>

int main() {
    std::atomic<int> v{ 0 };
    std::cout << v << '\n';
    ++v;
    std::cout << v << '\n';
    // v.fetch_mul(10);
    // std::atomic_ref
    std::atomic<int> lock{ 100 };
    std::thread thread([&lock]() {
        std::cout << "pre\n";
        lock.wait(100);
        std::cout << "post\n";
    });
    using namespace std::chrono_literals;
    std::this_thread::sleep_for(2s);
    std::cout << "= 100\n";
    lock = 100;
    lock.notify_one();
    std::this_thread::sleep_for(2s);
    std::cout << "= 10\n";
    lock = 10;
    lock.notify_one();
    std::this_thread::sleep_for(2s);
    std::cout << "= 0\n";
    lock = 0;
    lock.notify_one();
    thread.join();
    return 0;
}