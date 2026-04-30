#include <random>
#include <vector>
#include <iostream>

#include "torch/torch.h"
#include "torch/script.h"

#include "matplot/matplot.h"

#include "Dataset.hpp"

class LinearNetImpl : public torch::nn::Module {

private:
    torch::nn::Linear linear = nullptr;

public:
    LinearNetImpl();
    torch::Tensor forward(torch::Tensor x);

};

LinearNetImpl::LinearNetImpl() {
    this->linear = this->register_module("ln", torch::nn::Linear(1, 1));
}

torch::Tensor LinearNetImpl::forward(torch::Tensor x) {
    return this->linear(x);
}

TORCH_MODULE(LinearNet);

int main() {
    const float lr         = 0.1F;
    const int batch_size   = 20;
    const int epoch_count  = 128;
    const int dataset_size = 400;
    // 数据
    std::mt19937 rand(std::random_device{}());
    std::normal_distribution<float> w(10.0, 4.0);
    // std::normal_distribution<float> b( 1.0, 0.2);
    std::normal_distribution<float> b( 5.0, 1.0);
    std::vector<double> labels;
    std::vector<double> features;
    labels  .reserve(dataset_size);
    features.reserve(dataset_size);
    for(int index = 0; index < dataset_size; ++index) {
        float f = w(rand);
        float l = 5.8 * f + 2.6 + b(rand);
        // 5.8 * f + 2.6 + rand = l
        labels  .push_back(l);
        features.push_back(f);
    }
    std::vector<torch::Tensor> ls(dataset_size);
    std::vector<torch::Tensor> fs(dataset_size);
    std::transform(labels.begin(), labels.end(), ls.begin(), [](const auto& v){
        return torch::tensor({ v }, torch::kFloat32);
    });
    std::transform(features.begin(), features.end(), fs.begin(), [](const auto& v){
        return torch::tensor({ v }, torch::kFloat32);
    });
    auto dataset = Dataset(ls, fs).map(torch::data::transforms::Stack<>());
    auto loader  = torch::data::make_data_loader<LFT_RND_SAMPLER>(std::move(dataset), batch_size);
    // 定义
    LinearNet linear{};
    auto loss = torch::nn::MSELoss{};
    // auto optimizer = torch::optim::SGD{linear->parameters(), lr};
    auto optimizer = torch::optim::AdamW{linear->parameters(), lr};
    // 训练
    for(int epoch = 0; epoch < epoch_count; ++epoch) {
        float ttLoss = 0.0F;
        for(const auto& x : *loader) {
            auto output = linear->forward(x.data);
            auto tLoss  = loss(output, x.target);
            optimizer.zero_grad(); // 梯度清零
            tLoss.backward();      // 反向传播：计算梯度
            optimizer.step();      // 优化函数：更新参数
            ttLoss += tLoss.item<float>();
        }
        printf("all epoch : %d | loss : %.6f\n", epoch, ttLoss * batch_size / dataset_size);
    }
    // 预测
    linear->eval();
    std::cout << "pred : " << linear->forward(torch::tensor({ 1 }, torch::kFloat32)) << "\n";
    auto nameds = linear->named_parameters();
    for(auto entry = nameds.begin(); entry != nameds.end(); ++entry) {
        std::cout << "named_parameters k : " << entry->key() << " | v : " << entry->value() << "\n";
    }
    // 绘图
    matplot::hold(true);
    matplot::scatter(features, labels);
    auto px = matplot::linspace(0, 20, 10);
    auto py = matplot::transform(px, [&linear](auto v) {
        return linear->forward(torch::tensor({v}, torch::kFloat32)).item<float>();
    });
    matplot::plot(px, py);
    matplot::show();
    return 0;
}
