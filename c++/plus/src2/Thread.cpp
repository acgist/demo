#include <mutex>
#include <thread>
#include <chrono>
#include <iostream>
#include <condition_variable>

static std::mutex mutex;
static std::condition_variable condition;

int main() {
    std::thread threadA([]() {
        std::unique_lock<std::mutex> lock(mutex);
        // condition.wait(lock);
        condition.wait_until(lock, std::chrono::seconds(5) + std::chrono::system_clock::now());
    });
    std::thread threadB([]() {
        condition.notify_one();
    });
    threadB.detach();
    std::cout << "1\n";
    threadA.join();
    std::cout << "2\n";
    return 0;
}