#include <ctime>
#include <ratio>
#include <chrono>
#include <iomanip>
#include <iostream>

int main() {
    auto s = std::chrono::seconds(10);
    auto m = std::chrono::minutes(10);
    auto t = s + m;
    auto c = std::chrono::duration_cast<std::chrono::minutes>(s + m);
    std::cout << t.count() << '\n';
    std::cout << c.count() << '\n';
    auto now = std::chrono::system_clock::now();
    auto tt  = std::chrono::system_clock::to_time_t(now);
    auto tm  = std::localtime(&tt);
    std::cout << std::put_time(tm, "%H:%M:%S") << '\n';
    // auto diff = 1 <=> 2;
    return 0;
}
