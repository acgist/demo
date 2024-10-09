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
    // std::chrono::year_month_day ymd{ std::chrono::year{2024}, std::chrono::month{12}, std::chrono::day{12} };
    auto nowHours = std::chrono::floor<std::chrono::hours>(std::chrono::system_clock::now());
    std::cout << nowHours.time_since_epoch().count() << '\n';
    std::cout << std::chrono::duration_cast<std::chrono::hours>(nowHours.time_since_epoch()).count() << '\n';
    std::cout << std::chrono::duration_cast<std::chrono::minutes>(nowHours.time_since_epoch()).count() << '\n';
    std::cout << std::chrono::duration_cast<std::chrono::seconds>(nowHours.time_since_epoch()).count() << '\n';
    return 0;
}
