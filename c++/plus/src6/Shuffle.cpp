#include <random>
#include <iostream>
#include <iterator>
#include <algorithm>

int main() {
    std::random_device device{};
    std::default_random_engine rand(device());
    float* f = new float[] { 1.0F, 2.0F, 4.0F, 3.0F, 4.0F, 5.0F, 3.0F, 2.0F, 1.0F };
    std::copy(f, f + 9, std::ostream_iterator<float>(std::cout, " "));
    std::cout << '\n';
    std::shuffle(f, f + 9, rand);
    std::copy(f, f + 9, std::ostream_iterator<float>(std::cout, " "));
    std::cout << '\n';
    float s[]{ 1, 2, 3, 4, 5 };
    float t[3];
    std::sample(s, s + 5, t, 3, rand);
    std::copy(s, s + 5, std::ostream_iterator<float>(std::cout, " "));
    std::cout << '\n';
    std::copy(t, t + 3, std::ostream_iterator<float>(std::cout, " "));
    std::cout << '\n';
    return 0;
}