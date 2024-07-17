#include <chrono>
#include <memory>
#include <iostream>

int main() {
    std::chrono::time_point a = std::chrono::high_resolution_clock::now();
    for(int i = 0; i < 100000; ++i) {
        int* ip = new int { 0 };
        delete ip;
    }
    std::chrono::time_point z = std::chrono::high_resolution_clock::now();
    auto diff = std::chrono::duration_cast<std::chrono::milliseconds>(z - a);
    std::cout << "裸指针：" << diff.count() << '\n';
    a = std::chrono::high_resolution_clock::now();
    for(int i = 0; i < 100000; ++i) {
        // std::unique_ptr<int> up { std::make_unique<int>(0) };
        std::shared_ptr<int> up { std::make_shared<int>(0) };
    }
    z = std::chrono::high_resolution_clock::now();
    diff = std::chrono::duration_cast<std::chrono::milliseconds>(z - a);
    std::cout << "智能指针：" << diff.count() << '\n';
    return 0;
}