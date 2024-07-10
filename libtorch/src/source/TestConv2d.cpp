#include <iostream>

#include "torch/torch.h"

int main() {
    auto a = torch::ones({1, 1, 6, 8});
    std::cout << a << '\n';
    auto index = torch::tensor({ 2, 3, 4, 5 });
    // auto b = a.index_select(1, index);
    // b.fill_(0);
    // std::cout << b << '\n';
    a.index_fill_(3, index, 0);
    std::cout << a << '\n';
    // 卷积核
    auto k = torch::tensor({1, -1}, torch::kFloat);
    k.unsqueeze_(0);
    k.unsqueeze_(0);
    k.unsqueeze_(0);
    // 函数
    // std::cout << k << '\n';
    // torch::nn::functional::Conv2dFuncOptions options;
    // options.stride(1);
    // auto b = torch::nn::functional::conv2d(a, k, options);
    // 内置
    torch::nn::Conv2dOptions options{1, 1, {1, 2}};
    options.bias(false);
    torch::nn::Conv2d conv2d{options};
    auto x = conv2d->weight.data();
    // x.fill_(k);
    x.fill_(1);
    // x.fill_(torch::tensor({1, -1}, torch::kFloat));
    std::cout << "x : " << x << '\n';
    std::cout << "params : " << conv2d->parameters() << '\n';
    auto b = conv2d->forward(a);
    // 输出
    std::cout << a << '\n';
    std::cout << b << '\n';
    return 0;
}