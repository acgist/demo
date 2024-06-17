#include <mutex>
#include <thread>
#include <chrono>
#include <iostream>
#include <condition_variable>

static std::mutex mutex;
static std::condition_variable condition;

void fun(int a, int b, int c) {
    std::cout << a << b << c << "\n";
}

class P {

public:
    int say(int& a, const int& b, int c) {
        std::cout << &a << "\n";
        std::cout << &b << "\n";
        std::cout << a << b << c << "\n";
        return 0;
    }

};

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
    std::thread c(fun, 1, 2, 3);
    c.join();
    P p;
    int v = 1;
    int x = 2;
    std::cout << &v << "\n";
    std::cout << &x << "\n";
    std::thread d(P::say, &p, std::ref(v), x, 3);
    d.join();
    return 0;
}