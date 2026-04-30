#include <iostream>

#include "torch/torch.h"

int main() {
    auto embedding = torch::nn::Embedding(10, 3);
    auto input  = torch::tensor({{ 1, 2, 4, 5 }, { 4, 3, 2, 9 }});
    auto output = embedding(input);
    std::cout << output << '\n';
    return 0;
}
