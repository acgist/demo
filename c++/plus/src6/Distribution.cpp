#include <random>
#include <iostream>

int main() {
    std::mt19937 random(std::random_device{}());
    std::uniform_int_distribution distribution(1, 100);
    std::cout << distribution(random) << '\n';
    std::normal_distribution<float> normal(50, 10);
    for (size_t i = 0; i < 100; ++i) {
        std::cout << normal(random) << '\n';
    }
    // 均匀分布
    // std::uniform_int_distribution
    // std::uniform_real_distribution
    // 伯努利分布
    // std::binomial_distribution
    // std::bernoulli_distribution
    // std::geometric_distribution
    // std::negative_binomial_distribution
    // 泊松分布
    // std::poisson_distribution
    // std::gamma_distribution
    // std::weibull_distribution
    // std::exponential_distribution
    // std::extreme_value_distribution
    // 正态分布
    // std::normal_distribution
    // std::cauchy_distribution
    // std::fisher_f_distribution
    // std::student_t_distribution
    // std::lognormal_distribution
    // std::chi_squared_distribution
    // 采样分布
    // std::discrete_distribution
    // std::piecewise_linear_distribution
    // std::piecewise_constant_distribution
    return 0;
}