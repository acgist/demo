#include <vector>
#include <iostream>

#include "torch/torch.h"

int main() {
    auto a = torch::ones({2, 1, 5});
    auto b = torch::ones({2, 1, 5});
    // auto aLoss = torch::l1_loss(a, b);
    auto aLoss = torch::mse_loss(a, b);
    torch::nn::L1Loss l1Loss;
    auto bLoss = l1Loss(a, b);
    std::cout << "a = " << a << "\n";
    std::cout << "b = " << b << "\n";
    std::cout << aLoss << "\n";
    std::cout << bLoss << "\n";
    return 0;
}
