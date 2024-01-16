#include <thread>
#include <chrono>
#include <iostream>

void testTime() {
    std::chrono::system_clock::time_point a = std::chrono::system_clock::now();
    std::this_thread::sleep_for(std::chrono::seconds(2));
    std::chrono::system_clock::time_point b = std::chrono::system_clock::now();
    std::chrono::duration<long, std::milli> duration = std::chrono::duration_cast<std::chrono::duration<long, std::milli>>(b - a);
    std::cout << duration.count() << std::endl;
    std::cout << (b < a) << std::endl;
    // long time = std::chrono::time_point<std::chrono::system_clock, std::chrono::milliseconds>();
    // std::cout << time << std::endl;
    const long milli = std::chrono::milliseconds(1).count();
    std::cout << milli << std::endl;
}

void constCast() {
    const int* a = new int{0};
    // int* b = (int*) a;
    int* b = const_cast<int*>(a);
    std::cout << *a << std::endl;
    std::cout << *b << std::endl;
    *b = 1;
    std::cout << *a << std::endl;
    std::cout << *b << std::endl;
}

int main() {
    testTime();
    // constCast();
    return 0;
}