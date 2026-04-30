#include <chrono>
#include <random>
#include <iostream>

static std::vector<float> compute_reciprocals(std::vector<float> values) {
    std::vector<float> ret(values.size());
    for(int i = 0; i < values.size(); ++i) {
        ret[i] = 1.0 / values[i];
    }
    return ret;
}

int main() {
    std::vector<float> values(1000000);
    std::fill(values.begin(), values.end(), 1);
    auto a = std::chrono::system_clock::now();
    compute_reciprocals(values);
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count() << std::endl;
    return 0;
}