#include <ctime>
#include <random>
#include <iostream>
#include <functional>

int main() {
    srand(time(nullptr));
    std::cout << rand() << '\n';
    std::random_device rd;
    std::cout << "rd = " << rd() << '\n';
    std::mt19937 rand(rd());
    std::cout << "mt = " << rand() << '\n';
    std::minstd_rand mr(rd());
    std::cout << "mr = " << mr() << '\n';
    long long seed = rd.entropy() ? rd() : time(nullptr);
    std::cout << "et = " << rd.entropy() << '\n';
    std::uniform_int_distribution<int> distribution(1, 2);
    auto randGenerator = std::bind(distribution, rand);
    std::cout << "rg = " << randGenerator() << '\n';
    return 0;
}