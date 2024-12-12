#include <mutex>
#include <thread>
#include <iostream>

thread_local int x = 0;

class T {

public:
    thread_local static int y;

};

thread_local int T::y = 0;

static std::mutex mutex;

int main() {
    ++x;
    ++T::y;
    std::thread t1([]() {
        ++x;
        ++T::y;
        std::lock_guard<std::mutex> lock(mutex);
        std::cout << std::this_thread::get_id() << " t1 = " << x << '\n';
        std::cout << std::this_thread::get_id() << " t1 = " << T::y << '\n';
    });
    std::thread t2([]() {
        ++x;
        ++T::y;
        std::lock_guard<std::mutex> lock(mutex);
        std::cout << std::this_thread::get_id() << " t2 = " << x << '\n';
        std::cout << std::this_thread::get_id() << " t2 = " << T::y << '\n';
    });
    t1.join();
    t2.join();
    std::lock_guard<std::mutex> lock(mutex);
    std::cout << std::this_thread::get_id() << " = " << x << '\n';
    std::cout << std::this_thread::get_id() << " = " << T::y << '\n';
    return 0;
}
