#include <map>
#include <future>
#include <thread>
#include <chrono>
#include <iostream>

static std::map<int, std::promise<int>*> pm;

static std::promise<int>* promise = new std::promise<int>{};

int call() {
    return 0;
}

void wait() {
    std::this_thread::sleep_for(std::chrono::seconds(2));
    // promise.set_value(1);
    pm.at(1)->set_value(1);
}

int main() {
    std::future<int> future = std::async(std::launch::async, call);
    int ret = future.get();
    std::cout << ret;
    std::thread thread(wait);
    // thread.join();
    thread.detach();
    std::cout << 2;
    std::future<int> pf = promise->get_future();
    pm.insert({1, promise});
    if(pf.wait_for(std::chrono::seconds(5)) == std::future_status::timeout) {
        std::cout << 0;
    } else {
        std::cout << pf.get();
    }
    pm.erase(1);
    delete promise;
    std::cout << "+";
    auto now = std::chrono::system_clock::now();
    std::time_t t = std::chrono::system_clock::to_time_t(now);
    std::tm* now_tm = std::localtime(&t);
    std::cout << now_tm->tm_hour;
    std::cout << now_tm->tm_year;
    std::cout << now_tm->tm_mday;
    std::cout << now_tm->tm_min;
    std::cout << now_tm->tm_sec;
    return 0;
}