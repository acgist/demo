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
    std::default_random_engine e;
    // e.seed(std::time(0));
    std::cout << "e = " << e() << "\n";
    std::cout << "e = " << e() << "\n";
    // std::bernoulli_distribution distribution(0.8);
    // std::normal_distribution<double> distribution(10, 400);
    std::uniform_int_distribution<int> distribution(10, 400);
    // std::uniform_real_distribution<double> distribution(10, 400);
    std::cout << "e = " << distribution(e) << "\n";
    std::cout << "e = " << distribution(e) << "\n";
    return 0;
}
