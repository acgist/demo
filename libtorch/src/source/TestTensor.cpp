#include <iostream>

#include "torch/torch.h"
#include "torch/script.h"

int main() {
    // long long ai[] { 1, 2, 3 };
    // auto a = torch::from_blob(ai, {3, 1}, torch::kLong);
    // std::cout << a << "\n";
    // long long bi[] { 1, 2, 3 };
    // auto b = torch::tensor(bi, torch::kLong);
    // std::cout << b << "\n";
    // auto c = torch::tensor({ 1, 2, 3 }, torch::kLong);
    // std::cout << c << "\n";
    // auto d = torch::tensor({ 1, 2, 3 }, torch::kLong).unsqueeze(1);
    // std::cout << d << "\n";
    // std::cout << d.sizes() << "\n";
    // std::cout << d.sizes()[0] << "\n";

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
    
    // auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    // std::cout << t.sum() << "\n";
    // std::cout << t.sum(0, true) << "\n";
    // std::cout << t.sum(1, true) << "\n";
    // std::cout << t.sum(0, false) << "\n";
    // std::cout << t.sum(1, false) << "\n";

    // auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    // // auto e = t.clone();
    // auto e = t.exp();
    // auto s = e.sum(1, true);
    // std::cout << t << "\n";
    // std::cout << e << "\n";
    // std::cout << s << "\n";
    // auto softmax = t / s;
    // std::cout << softmax << "\n";

    // auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    // // std::cout << t.view({-1, 4}) << "\n";
    // std::cout << t.view({-1, 3}) << "\n";
    // std::cout << t.view({-1, 2}) << "\n";
    // std::cout << t.view({-1}) << "\n";
    // std::cout << t.flatten() << "\n";
    // std::cout << t.flatten(1) << "\n";

    auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    std::cout << t.argmax()  << "\n";
    std::cout << t.argmax(1) << "\n";
    // t.grad().data().zero_();
    return 0;
}
