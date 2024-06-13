#include <random>
#include <iostream>

int main() {
    std::random_device device;
    std::mt19937 rand(device());
    std::normal_distribution<> normal(20, 5);
    for(int i = 0; i < 100; ++i) {
        std::cout << normal(rand) << "\n";
    }
    return 0;
}