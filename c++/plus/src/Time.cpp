#include <ctime>
#include <chrono>
#include <iostream>

int main(int argc, char const *argv[]) {
    auto clockNow = std::chrono::system_clock::now();
    auto clockMilli = std::chrono::time_point_cast<std::chrono::milliseconds>(clockNow);
    std::cout << clockMilli.time_since_epoch().count() << std::endl;
    std::cout << std::time(nullptr) << std::endl;
    std::cout << std::clock() << std::endl;
    long long now = std::clock();
    while(std::clock() - now < CLOCKS_PER_SEC) {
    }
    clockNow = std::chrono::system_clock::now();
    clockMilli = std::chrono::time_point_cast<std::chrono::milliseconds>(clockNow);
    std::cout << clockMilli.time_since_epoch().count() << std::endl;
    std::cout << std::time(nullptr) << std::endl;
    std::cout << std::clock() << std::endl;
    std::cout << std::clock() << std::endl;
    return 0;
}
