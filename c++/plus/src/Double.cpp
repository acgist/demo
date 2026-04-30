#include <iomanip>
#include <format>
#include <iostream>

int main() {
    std::cout << 1.0F / 90000 << std::endl;
    // 全局
    // std::cout.precision(6);
    // std::cout.flags(std::cout.fixed);
    // std::cout.unsetf(cout.fixed);
    std::cout << std::setprecision(6) << std::fixed << 1.0F / 90000 << std::endl;
    std::printf("%.6f\r", 1.0F / 90000);
    std::printf("%.6f\r", 1.0F / 90000);
    std::printf("%.6f\r", 1.0F / 90000);
    // std::cout.setf(std::ios_base::fixed, std::ios_base::floatfield);
    std::cout << 1.0F / 90000 << std::endl;
    return 0;
}