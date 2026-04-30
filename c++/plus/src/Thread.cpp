#include <mutex>
#include <thread>
#include <iostream>

std::mutex lock;
// 这个会自动加锁
// std::unique_lock<std::mutex> uniqueLock(lock);

int count = 0;

void count1() {
    int i = 0;
    while(++i <= 100000) {
        lock.lock();
        count++;
        lock.unlock();
    }
}

void count2() {
    int i = 0;
    while(++i <= 100000) {
        lock.lock();
        count++;
        lock.unlock();
    }
}

int main() {
    // lock.unlock();
    std::thread thread1(count1);
    std::thread thread2(count2);
    std::thread thread3(count1);
    std::thread thread4(count2);
    thread1.join();
    thread2.join();
    thread3.join();
    thread4.join();
    std::cout << count << "\r\n";
    return 0;
}