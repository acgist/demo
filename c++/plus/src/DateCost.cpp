#include <ctime>
#include <chrono>
#include <string>
#include <iomanip>
#include <sstream>
#include <iostream>

#ifndef LFR_DATE_TIME_FORMAT
#define LFR_DATE_TIME_FORMAT "%Y-%m-%d %H:%M:%S"
#endif

std::string formatEx(const std::tm& datetime, const std::string& format) {
    std::ostringstream output;
    output << std::put_time(&datetime, format.c_str());
    return output.str();
    // char x[100];
    // strftime(x, 100, format.c_str(), &datetime);
    // return x;
}

std::string format(const std::chrono::system_clock::time_point& datetime, const std::string& format) {
    const std::time_t timestamp = std::chrono::system_clock::to_time_t(datetime);
    // const std::tm tm = *std::gmtime(&timestamp);
    const std::tm tm = *std::localtime(&timestamp);
    return formatEx(tm, format);
}

int main() {
    const std::chrono::system_clock::time_point a = std::chrono::system_clock::now();
    for(int index = 0; index < 100000; ++index) {
        format(a, LFR_DATE_TIME_FORMAT);
        // std::cout << format(a, LFR_DATE_TIME_FORMAT) << std::endl;
        // std::cout << format(a, LFR_DATE_TIME_FORMAT).length() << std::endl;
    }
    const std::chrono::system_clock::time_point z = std::chrono::system_clock::now();
    std::cout << "耗时：" << std::chrono::duration_cast<std::chrono::milliseconds>((z - a)).count() << std::endl;
    return 0;
}