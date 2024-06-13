#include <iostream>

#include "torch/torch.h"
#include "torch/script.h"

int main() {
    long long ai[] { 1, 2, 3 };
    auto a = torch::from_blob(ai, {3, 1}, torch::kLong);
    std::cout << a << "\n";
    long long bi[] { 1, 2, 3 };
    auto b = torch::tensor(bi, torch::kLong);
    std::cout << b << "\n";
    auto c = torch::tensor({ 1, 2, 3 }, torch::kLong);
    std::cout << c << "\n";
    auto d = torch::tensor({ 1, 2, 3 }, torch::kLong).unsqueeze(1);
    std::cout << d << "\n";
    std::cout << d.sizes() << "\n";
    std::cout << d.sizes()[0] << "\n";

    // auto a = torch::ones({1, 2});
    // auto b = torch::ones({2, 3});
    // std::cout << (a * a) << "\n";
    // std::cout << (a.mul(a)) << "\n";
    // std::cout << (torch::mm(a, b)) << "\n";
    // std::cout << (torch::matmul(a, b)) << "\n";

    // std::vector<float> v = {
    //     2.1, 3.3, 3.6, 4.4, 5.5, 6.3, 6.5, 7.0, 7.5, 9.7, // x
    //     1.0, 1.2, 1.9, 2.0, 2.5, 2.5, 2.2, 2.7, 3.0, 3.6  // y
    // }; 
    // torch::Tensor data_tensor = torch::from_blob(v.data(), {2, 10}, torch::kFloat32);
    // std::cout << data_tensor << "\n";
    // std::cout << data_tensor.transpose(0, 1) << "\n";

    // auto a = torch::ones({2, 3});
    // auto b = torch::ones({2, 3});
    // std::cout << torch::cat({a, b}, 0) << "\n";
    // std::cout << torch::cat({a, b}, 1) << "\n";
    // std::cout << torch::cat({a, b}, 0).sizes() << "\n";
    // std::cout << torch::cat({a, b}, 1).sizes() << "\n";

    // auto a = torch::ones({2, 3});
    // auto b = torch::ones({2, 1});
    // std::cout << torch::cat({a, b}, 1) << "\n";
    // std::cout << torch::cat({a, b}, 1).sizes() << "\n";

    // auto a = torch::ones({2, 3});
    // auto b = torch::ones({2}).unsqueeze(1);
    // std::cout << b.sizes() << "\n";
    // std::cout << torch::cat({a, b}, 1) << "\n";
    // std::cout << torch::cat({a, b}, 1).sizes() << "\n";
    return 0;
}
