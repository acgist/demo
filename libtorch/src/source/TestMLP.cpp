#include <random>
#include <iostream>

#include "../header/Datasets.hpp"

#include "torch/torch.h"
#include "torch/script.h"

class MLPImpl : public torch::nn::Module {

public:
    torch::nn::Sequential mlp = nullptr;

public:
    MLPImpl();
    ~MLPImpl();
    torch::Tensor forward(torch::Tensor x);

};

TORCH_MODULE(MLP);

torch::Tensor MLPImpl::forward(torch::Tensor x) {
    // return this->mlp(x);
    return this->mlp->forward(x);
}

MLPImpl::MLPImpl() {
    torch::nn::Sequential mlp;
    mlp->push_back(torch::nn::Linear(2, 2));
    mlp->push_back(torch::nn::ReLU());
    // mlp->push_back(torch::nn::ReLU(torch::nn::ReLUOptions(true)));
    // mlp->push_back(torch::nn::Dropout());
    mlp->push_back(torch::nn::Linear(2, 1));
    this->mlp = register_module("mlp", mlp);
}

MLPImpl::~MLPImpl() {
}

int main() {
    // const float lr = 0.001F;
    // const float lr = 0.002F;
    // const float lr = 0.003F;
    // const float lr = 0.004F;
    const float lr = 0.005F;
    const int batch_size  = 10;
    // const int epoch_count = 8;
    const int epoch_count = 128;
    // const int epoch_count = 256;
    // 准备数据
    std::random_device device;
    std::mt19937 rand(device());
    std::normal_distribution<> normal(20, 5);
    torch::Tensor features = torch::randn({ 1000, 2 });
    // 15.8 * a + 87.4 * b + 32 + rand = c
    torch::Tensor labels = features.select(1, 0) * 15.8 + features.select(1, 1) * 87.4 + 32 + normal(rand);
    auto dataset = lifuren::datasets::TensorDataset(features, labels.unsqueeze(1)).map(torch::data::transforms::Stack<>());
    auto loader = torch::data::make_data_loader<torch::data::samplers::RandomSampler>(dataset, batch_size);
    MLP mlp;
    torch::nn::MSELoss loss{};
    torch::optim::SGD optimizer{mlp->parameters(), lr};
    for(int epoch = 0; epoch < epoch_count; ++epoch) {
        float ttLoss = 0.0;
        for(auto& data : *loader) {
            optimizer.zero_grad();
            auto output = mlp->forward(data.data);
            auto tLoss  = loss(output, data.target);
            tLoss.backward();
            optimizer.step();
            ttLoss += tLoss.item<float>();
        }
        printf("all epoch : %d | loss : %f\n", epoch, ttLoss * 10 / 1000);
        if(ttLoss <= 0.000001F) {
            // 满足损失退出训练
            break;
        }
    }
    std::cout << "pred : " << mlp->forward(torch::tensor({1, 1}, torch::kFloat32)) << "\n";
    auto nameds = mlp->named_parameters();
    for(auto entry = nameds.begin(); entry != nameds.end(); ++entry) {
        std::cout << "named_parameters k : " << entry->key() << " | v : " << entry->value() << "\n";
    }
    return 0;
}
