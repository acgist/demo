#include <random>
#include <iostream>

#include "torch/torch.h"

#include "../header/Datasets.hpp"

// 是否已经配置DEGREE
#define DEGREE false

class MultiLineImpl : public torch::nn::Module {

public:
    #if DEGREE
    torch::nn::Linear linear = nullptr;
    #else
    torch::nn::Sequential linear = nullptr;
    #endif

public:
    MultiLineImpl();
    ~MultiLineImpl();

public:
    torch::Tensor forward(torch::Tensor x);

};

TORCH_MODULE(MultiLine);

MultiLineImpl::MultiLineImpl() {
    #if DEGREE
    this->linear = register_module("linear", torch::nn::Linear(3, 1));
    #else
    torch::nn::Sequential linear;
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::ReLU{});
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::ReLU{});
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::ReLU{});
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::ReLU{});
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::ReLU{});
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::ReLU{});
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::Linear{3, 3});
    linear->push_back(torch::nn::BatchNorm1d{3});
    linear->push_back(torch::nn::ReLU{});
    linear->push_back(torch::nn::Linear{3, 1});
    this->linear = register_module("linear", linear);
    #endif
}

MultiLineImpl::~MultiLineImpl() {
    unregister_module("linear");
}

torch::Tensor MultiLineImpl::forward(torch::Tensor x) {
    return this->linear->forward(x);
}

int main() {
    #if DEGREE
    const float lr = 0.001F;
    #else
    const float lr = 0.01F;
    #endif
    const size_t batch_size = 10;
    // const size_t epoch_count = 16;
    #if DEGREE
    const size_t epoch_count = 64;
    #else
    const size_t epoch_count = 64;
    // const size_t epoch_count = 128;
    #endif
    std::mt19937 random{std::random_device{}()};
    std::normal_distribution<> normal{4, 1};
    // std::normal_distribution<int> normal{4, 1};
    // x1 * 1.2 + x2 ^ 2 * 5.6 + x3 ^ 3 * 9.6 + 10 + random
    #if DEGREE
    // 不计算degree
    auto features = torch::cat({torch::randn({1000, 1}), torch::randn({1000, 1}).pow(2), torch::randn({1000, 1}).pow(3)}, 1);
    auto labels = features.select(1, 0) * 1.2 + features.select(1, 1) * 5.6 + features.select(1, 2) * 9.6 + 10 + (torch::randn({1000}) / 10);
    // auto labels = features.select(1, 0) * 1.2 + features.select(1, 1) * 5.6 + features.select(1, 2) * 9.6 + 10 + normal(random);
    #else
    // 要计算degree
    auto features = torch::randn({1000, 3}) * 10;
    auto labels = features.select(1, 0) * 1.2 + torch::pow(features.select(1, 1), 2) * 5.6 + torch::pow(features.select(1, 2), 3) * 9.6 + 10 + torch::randn({1000});
    // auto labels = features.select(1, 0) * 1.2 + torch::pow(features.select(1, 1), 2) * 5.6 + torch::pow(features.select(1, 2), 3) * 9.6 + 10 + (torch::randn({1000}) / 10);
    // auto labels = features.select(1, 0) * 1.2 + torch::pow(features.select(1, 1), 2) * 5.6 + torch::pow(features.select(1, 2), 3) * 9.6 + 10 + normal(random);
    #endif
    std::cout << features.sizes() << '\n';
    std::cout << labels.sizes()   << '\n';
    std::cout << features[0]      << '\n';
    std::cout << labels[0]        << '\n';
    MultiLine line{};
    torch::nn::L1Loss loss{};
    // torch::nn::MSELoss loss{};
    // torch::optim::SGD optimizer{line->parameters(), lr};
    torch::optim::SGDOptions options{lr};
    options.weight_decay(0.01F);
    torch::optim::SGD optimizer{line->parameters(), options};
    auto dataset = lifuren::datasets::TensorDataset{features, labels.unsqueeze(1)}.map(torch::data::transforms::Stack<>());
    auto loader  = torch::data::make_data_loader<torch::data::samplers::RandomSampler>(dataset, batch_size);
    for(size_t epoch = 0; epoch < epoch_count; ++epoch) {
        float ttLoss = 0.0F;
        for(auto& data : *loader) {
            optimizer.zero_grad();
            auto output = line->forward(data.data);
            auto tLoss  = loss(output, data.target);
            tLoss.backward();
            optimizer.step();
            ttLoss += tLoss.item<float>();
        }
        std::printf("epoch : %lld | loss : %f\n", epoch, ttLoss * 10 / 1000);
    }
    auto names = line->named_parameters();
    auto iterator = names.begin();
    auto end      = names.end();
    for(; iterator != end; ++iterator) {
        std::cout << iterator->key() << " = " << iterator->value() << '\n';
    }
    // Dropout和BatchNorm不再生效
    line->eval();
    try {
        // x1 * 1.2 + x2 ^ 2 * 5.6 + x3 ^ 3 * 9.6 + 10 + random
        auto
        ret = line->forward(torch::tensor({1.0F, 2.0F, 3.0F}).unsqueeze(0));
        std::cout << ret << '\n';
        ret = line->forward(torch::tensor({1.0F, 1.0F, 1.0F}).unsqueeze(0));
        std::cout << ret << '\n';
        ret = line->forward(torch::tensor({3.0F, 2.0F, 1.0F}).unsqueeze(0));
        std::cout << ret << '\n';
        ret = line->forward(torch::tensor({2.0F, 2.0F, 2.0F}).unsqueeze(0));
        std::cout << ret << '\n';
        torch::save(line, "D:/tmp/x.pt");
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
    return 0;
}
