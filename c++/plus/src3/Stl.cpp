#include <vector>
#include <memory>
#include <chrono>
#include <iostream>
#include <algorithm>
#include <functional>

int main() {
    std::vector<int> vector{0, 3, 6, 3, 3, 4, 5, 2, 1, 1};
    std::sort(vector.begin(), vector.end());
    for(auto& x : vector) {
        std::cout << x << " = ";
    }
    std::cout << "\n";
    // std::sort(vector.begin(), vector.end(), std::greater<int>());
    std::sort(vector.begin(), vector.end(), std::not_fn(std::less<int>()));
    // std::sort(vector.begin(), vector.end(), std::not2(std::less<int>()));
    for(auto& x : vector) {
        std::cout << x << " = ";
    }
    std::cout << "\n";
    for(auto i = vector.begin(); i != vector.end();) {
        if(*i == 3) {
            // vector.erase(i++);
            i = vector.erase(i);
        } else {
            ++i;
        }
    }
    for(auto& x : vector) {
        std::cout << x << " = ";
    }
    std::cout << "\n";
    auto a = std::chrono::system_clock::now();
    auto end = vector.end();
    for(int i = 0; i < 100'000; ++i) {
        for(auto x = vector.begin(); x != end; ++x) {
        // for(auto x = vector.begin(); x != vector.end(); ++x) {
        }
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    a = std::chrono::system_clock::now();
    for(int i = 0; i < 100'000; ++i) {
        std::for_each(vector.begin(), end, [](auto& x) {
        // std::for_each(vector.begin(), vector.end(), [](auto& x) {
        });
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    return 0;
}