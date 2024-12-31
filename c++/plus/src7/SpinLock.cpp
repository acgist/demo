#include <mutex>
#include <atomic>
#include <chrono>
#include <thread>
#include <iostream>

static std::mutex mutex;
static std::atomic_flag flag = ATOMIC_FLAG_INIT;

int i = 0;

int main() {
    // static int i = 0;
    for(int index = 0; index < 100; ++index) {
        std::thread t([/* &i */]() {
            for (size_t j = 0; j < 10000; j++) {
                ++i;
            }
        });
        t.detach();
    }
    using namespace std::chrono_literals;
    std::this_thread::sleep_for(2s);
    std::cout << "without lock : " << i << '\n';
    i = 0;
    for(int index = 0; index < 100; ++index) {
        std::thread t([/* &i */]() {
            for (size_t j = 0; j < 10000; j++) {
                std::lock_guard<std::mutex> lock(mutex);
                ++i;
            }
        });
        t.detach();
    }
    std::this_thread::sleep_for(2s);
    std::cout << "without lock : " << i << '\n';
    i = 0;
    for(int index = 0; index < 100; ++index) {
        std::thread t([/* &i */]() {
            for (size_t j = 0; j < 10000; j++) {
                while(flag.test_and_set()) {};
                ++i;
                flag.clear();
            }
        });
        t.detach();
    }
    std::this_thread::sleep_for(2s);
    std::cout << "without lock : " << i << '\n';
    return 0;
}