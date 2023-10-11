#include <ctime>
#include <random>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::srand(std::time(0));
    std::cout << "rand = " << std::rand() << std::endl;
    std::cout << "rand = " << std::rand() << std::endl;
    std::random_device randomDevice;
    std::cout << "randomDevice = " << randomDevice() << std::endl;
    std::cout << "randomDevice = " << randomDevice() << std::endl;
    std::mt19937 mt(std::random_device{}());
    // std::mt19937 mt(std::random_device{}());
    std::cout << "mt19937 = " << mt() << std::endl;
    std::cout << "mt19937 = " << mt() << std::endl;
    return 0;
}
