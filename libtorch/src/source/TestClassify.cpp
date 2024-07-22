#include <random>
#include <iostream>

#include "torch/torch.h"

#include "../header/Datasets.hpp"

class ClassifyImpl : public torch::nn::Module {

public:
    torch::nn::Sequential mlp = nullptr;

public:
    ClassifyImpl();
    ~ClassifyImpl();
    torch::Tensor forward(torch::Tensor x);

};

TORCH_MODULE(Classify);

ClassifyImpl::ClassifyImpl() {
    torch::nn::Sequential mlp;
    // line
    mlp->push_back(torch::nn::Linear(2, 3));
    // mlp
    // mlp->push_back(torch::nn::Linear(2, 4));
    // mlp->push_back(torch::nn::Tanh());
    // // mlp->push_back(torch::nn::ReLU());
    // // mlp->push_back(torch::nn::Dropout());
    // mlp->push_back(torch::nn::Linear(4, 4));
    // mlp->push_back(torch::nn::Tanh());
    // // mlp->push_back(torch::nn::ReLU());
    // // mlp->push_back(torch::nn::Dropout());
    // mlp->push_back(torch::nn::Linear(4, 3));
    this->mlp = register_module("classify", mlp);
}

ClassifyImpl::~ClassifyImpl() {
    unregister_module("classify");
}

torch::Tensor ClassifyImpl::forward(torch::Tensor x) {
    x = this->mlp->forward(x);
    // return torch::softmax(x, 1);
    return torch::log_softmax(x, 1);
}

using DatasetType = torch::data::datasets::MapDataset<lifuren::datasets::TensorDataset, torch::data::transforms::Stack<torch::data::Example<>>>;

void train(Classify& classify, DatasetType& dataset, const float& lr, const size_t& batch_size, const size_t& epoch_count) {
    auto loader = torch::data::make_data_loader<torch::data::samplers::RandomSampler>(dataset, batch_size);
    // auto loader = torch::data::make_data_loader<torch::data::samplers::SequentialSampler>(dataset, batch_size);
    // torch::nn::NLLLoss loss{};
    torch::nn::CrossEntropyLoss loss{};
    // torch::optim::SGD optimizer(classify->parameters(), lr);
    torch::optim::Adam optimizer(classify->parameters(), lr);
    for(size_t epoch = 0; epoch < epoch_count; ++epoch) {
        size_t index  = 0;
        size_t ttAcc  = 0;
        float  ttLoss = 0.0F;
        for(auto& data : *loader) {
            ++index;
            optimizer.zero_grad();
            auto output = classify->forward(data.data);
            auto tAcc   = output.argmax(1).eq(data.target).sum();
            auto tLoss  = loss(output, data.target);
            tLoss.backward();
            optimizer.step();
            ttAcc  += tAcc.template item<int>();
            ttLoss += tLoss.template item<float>();
            printf("train epoch : %lld | acc : %lld | loss : %f | index : %lld\r", epoch, ttAcc, ttLoss / index, index);
            std::flush(std::cout);
        }
        std::cout << '\n';
    }
}

void test(Classify& classify, DatasetType& dataset, const float& lr, const size_t& batch_size, const size_t& epoch_count) {
    classify->eval();
    auto loader = torch::data::make_data_loader<torch::data::samplers::RandomSampler>(dataset, batch_size);
    size_t ttAcc = 0;
    float ttLoss = 0.0F;
    // 可以不用损失函数
    // torch::nn::NLLLoss loss{};
    torch::nn::CrossEntropyLoss loss{};
    for(auto& data : *loader) {
        auto output = classify->forward(data.data);
        auto tAcc   = output.argmax(1).eq(data.target).sum();
        auto tLoss  = loss(output, data.target);
        ttAcc  += tAcc.template item<int>();
        ttLoss += tLoss.template item<float>();
    }
    printf("test acc : %lld | loss : %f\n", ttAcc, ttLoss * 10 / 1500);
}

int main() {
    // const float lr = 0.01F;
    const float lr = 0.05F;
    // const float lr = 0.10F;
    const size_t batch_size  = 30;
    const size_t epoch_count = 32;
    std::mt19937 random(std::random_device{}());
    std::normal_distribution<> normal(5, 1);
    int typeA = 100;
    int typeB = 200;
    int typeC = 300;
    // int typeA = normal(random) + 100;
    // int typeB = normal(random) + 300;
    // int typeC = normal(random) + 500;
    std::cout << "type a = " << typeA << " "
              << "type b = " << typeB << " "
              << "type c = " << typeC << "\n";
    // 不要用乘效果不好
    torch::Tensor featuresA = torch::randint(typeA - 20, typeA, {600, 2}) / typeA;
    torch::Tensor featuresB = torch::randint(typeC - 20, typeC, {600, 2}) / typeB;
    torch::Tensor featuresC = torch::randint(typeC - 20, typeC, {600, 2}) / typeC;
    torch::Tensor labelA = torch::empty({600}, torch::kLong).fill_(0);
    torch::Tensor labelB = torch::empty({600}, torch::kLong).fill_(1);
    torch::Tensor labelC = torch::empty({600}, torch::kLong).fill_(2);
    torch::Tensor testA = torch::randint(typeA - 20, typeA, {600, 2}) / typeA;
    torch::Tensor testB = torch::randint(typeC - 20, typeC, {600, 2}) / typeB;
    torch::Tensor testC = torch::randint(typeC - 20, typeC, {600, 2}) / typeC;
    auto trainTensor = torch::cat({featuresA, featuresB, featuresC}, 0);
    auto testTensor  = torch::cat({testA, testB, testC}, 0);
    auto labelTensor = torch::cat({labelA, labelB, labelC}, 0);
    auto trainData = lifuren::datasets::TensorDataset(
        trainTensor,
        labelTensor
    ).map(torch::data::transforms::Stack<>());
    auto testData = lifuren::datasets::TensorDataset(
        testTensor,
        labelTensor
    ).map(torch::data::transforms::Stack<>());
    std::cout << "train data size : " << trainTensor.sizes() << '\n';
    std::cout << "test data size : "  << testTensor.sizes() << '\n';
    Classify classify;
    try {
        train(classify, trainData, lr, batch_size, epoch_count);
        test(classify, testData, lr, batch_size, epoch_count);
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
    return 0;
}