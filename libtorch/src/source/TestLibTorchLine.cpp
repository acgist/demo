/**
 * https://github.com/ggerganov/ggml/blob/master/examples/mnist/mnist-common.h
 * https://github.com/ggerganov/ggml/blob/master/examples/mnist/mnist-common.cpp
 * https://github.com/ggerganov/ggml/blob/master/examples/mnist/mnist-train.cpp
 */
#include <random>
#include <vector>
#include <format>
#include <iostream>

#include "../header/Datasets.hpp"

#include "torch/torch.h"
#include "torch/script.h"

#include "matplot/matplot.h"

class LinearNetImpl : public torch::nn::Module {

private:
    torch::nn::Linear linear = nullptr;

public:
    LinearNetImpl();
    torch::Tensor forward(torch::Tensor x);
    // torch::Tensor forward(torch::Tensor& x);

};

LinearNetImpl::LinearNetImpl() {
    // this->linear = torch::nn::Linear(2, 1);
    this->linear = register_module("ln", torch::nn::Linear(2, 1));
}

torch::Tensor LinearNetImpl::forward(torch::Tensor x) {
    return this->linear(x);
}

TORCH_MODULE(LinearNet);

int main() {
    try {
        const float lr = 0.01F;
        const int batch_size  = 10;
        const int epoch_count = 32;
        // 准备数据
        std::random_device device;
        std::mt19937 rand(device());
        std::normal_distribution<> normal(20, 5);
        torch::Tensor features = torch::randn({ 1000, 2 });
        // features.set_requires_grad(true);
        // 15.8 * a + 87.4 * b + 32 + rand = c
        // torch::Tensor labels = features.select(1, 0) * 15.8 + features.select(1, 1) * 87.4 + 32 + normal(rand);
        torch::Tensor labels = features.select(1, 0) * 15.8 + features.select(1, 1) * 87.4 + 32 + (torch::randn({1000}) * 4);
        // labels.set_requires_grad(true);
        // std::cout << labels.sizes()   << "\n";
        // std::cout << features.sizes() << "\n";
        auto dataset = lifuren::datasets::TensorDataset(features, labels.unsqueeze(1)).map(torch::data::transforms::Stack<>());
        auto loader = torch::data::make_data_loader<torch::data::samplers::RandomSampler>(dataset, batch_size);
        // 模型定义
        LinearNet linear{};
        auto loss = torch::nn::MSELoss{};
        auto optimizer = torch::optim::SGD{linear->parameters(), lr};
        // 开始训练
        for(int epoch = 0; epoch < epoch_count; ++epoch) {
            float ttLoss = 0.0F;
            for(auto& x : *loader) {
                // std::cout << "data   = " << x.data   << "\n";
                // std::cout << "target = " << x.target << "\n";
                // std::cout << "data   = " << x.data.sizes()   << "\n";
                // std::cout << "target = " << x.target.sizes() << "\n";
                auto output = linear->forward(x.data);
                auto tLoss = loss(output, x.target);
                // auto tLoss = torch::mse_loss(output, x.target);
                // auto tLoss = loss->forward(output, x.target);
                // 梯度清零
                optimizer.zero_grad();
                tLoss.backward();
                optimizer.step();
                // printf("per epoch : %d | loss : %f\n", epoch, tLoss.item<float>());
                ttLoss += tLoss.item<float>();
            }
            std::cout << std::format("all epoch : {} | loss : {:8.6f}\n", epoch, ttLoss * 10 / 1000);
            // printf("all epoch : %d | loss : %f\n", epoch, ttLoss * 10 / 1000);
        }
        std::cout << "pred : " << linear->forward(torch::tensor({1, 1}, torch::kFloat32)) << "\n";
        auto params = linear->parameters();
        for(auto param = params.begin(); param != params.end(); ++param) {
            std::cout << "parameters " << param->data() << "\n";
        }
        auto nameds = linear->named_parameters();
        for(auto entry = nameds.begin(); entry != nameds.end(); ++entry) {
            std::cout << "named_parameters k : " << entry->key() << " | v : " << entry->value() << "\n";
        }
        matplot::hold(true);
        auto xp = features.select(1, 0).data_ptr<float>();
        auto yp = features.select(1, 1).data_ptr<float>();
        auto zp = labels.data_ptr<float>();
        std::vector<double> x(xp, xp + 1000);
        std::vector<double> y(yp, yp + 1000);
        std::vector<double> z(zp, zp + 1000);
        matplot::scatter3(x, y, z);
        auto px = matplot::linspace(-10, 10, 100);
        auto py = matplot::linspace(-10, 10, 100);
        auto pz = matplot::transform(px, py, [&linear](auto a, auto b) {
            return linear->forward(torch::tensor({a, b}, torch::kFloat32)).item<float>();
        });
        matplot::plot3(px, py, pz);
        matplot::show();
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
    return 0;
}
