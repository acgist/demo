#include <iostream>

#include "torch/torch.h"

int main() {
    std::cout << torch::cuda::is_available() << '\n';
    torch::Tensor tensor = torch::randn({3, 4});
    std::cout << tensor << '\n';
    // std::cout << tensor.cuda();
    std::cout << tensor.device() << '\n';
    return 0;
}