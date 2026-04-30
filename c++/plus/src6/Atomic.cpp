#include <mutex>
#include <atomic>
#include <thread>
#include <chrono>
#include <iostream>

int main() {
    // int count = 0;
    std::mutex mutex;
    std::atomic<int> count(0);
    for(int i = 0; i < 20; ++i) {
        std::thread t([&count, &mutex]() {
            for(int j = 0; j < 100000; ++j) {
                // std::lock_guard<std::mutex> lock(mutex);
                // count++;
                // ++count;
                // ++count;
                // count++;
                count.fetch_add(1);
            }
        });
        t.detach();
    }
    std::this_thread::sleep_for(std::chrono::seconds(1));
    std::cout << count << '\n';
    return 0;
}