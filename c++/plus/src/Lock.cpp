#include <mutex>
#include <iostream>

static int a = 0;
static std::recursive_mutex mutex;
// static std::mutex mutex;

void fun() {
    std::lock_guard<std::recursive_mutex> lockc(mutex);
    std::cout << "3\n";
}

int main() {
    std::lock_guard<std::recursive_mutex> locka(mutex);
    std::cout << "1\n";
    std::lock_guard<std::recursive_mutex> lockb(mutex);
    std::cout << "2\n";
    fun();
    return 0;
}