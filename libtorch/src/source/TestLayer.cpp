#include <iostream>

#include "torch/torch.h"

int main() {
    torch::nn::Sequential sequential;
    torch::nn::ModuleList moduleList;
    torch::nn::ModuleDict moduleDict;
    sequential->push_back(torch::nn::Linear{2, 1});
    sequential->push_back(torch::nn::ReLU{});
    moduleList->push_back(torch::nn::Linear{2, 1});
    moduleList->push_back(torch::nn::ReLU{});
    // auto ss = std::make_shared<>(sequential);
    // auto sm = std::make_shared<>(moduleList);
    // moduleDict->insert("1", ss);
    // moduleDict->insert("2", sm);
    // moduleDict->insert("1", torch::nn::Linear{2, 1});
    // moduleDict->insert("2", torch::nn::ReLU{});
    std::cout << sequential << '\n';
    std::cout << moduleList << '\n';
    std::cout << moduleDict << '\n';
    return 0;
}