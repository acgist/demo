#include <chrono>
#include <format>
#include <iostream>

int main() {
    auto a = std::chrono::system_clock::now();
    for(int i = 0; i < 10'000; ++i) {
        std::cout << std::format("性能测试：{} {}\n", "测试", 0.4F);
        // std::cout << "性能测试：" << "测试 " << 0.4F << '\n';
        // printf("性能测试：%s %f\n", "测试", 0.4F);
        // std::printf("性能测试：%s %f\n", "测试", 0.4F);
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << "\n";
    return 0;
}