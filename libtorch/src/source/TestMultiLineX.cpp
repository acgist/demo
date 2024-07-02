#include <cmath>
#include <random>
#include <iostream>

#include "torch/torch.h"

#include "../header/Datasets.hpp"

#include "matplot/matplot.h"

class MultiLineImpl : public torch::nn::Module {

public:
    torch::nn::Linear linear = nullptr;

public:
    MultiLineImpl();
    ~MultiLineImpl();

public:
    torch::Tensor forward(torch::Tensor x);

};

TORCH_MODULE(MultiLine);

MultiLineImpl::MultiLineImpl() {
    this->linear = register_module("linear", torch::nn::Linear(3, 1));
}

MultiLineImpl::~MultiLineImpl() {
    unregister_module("linear");
}

torch::Tensor MultiLineImpl::forward(torch::Tensor x) {
    return this->linear->forward(x);
}

void linePlot(const torch::Tensor& x, const torch::Tensor& labels, const int count, MultiLine& line) {
    matplot::hold(true);
    auto xp = x.data_ptr<float>();
    auto yp = labels.data_ptr<float>();
    std::vector<double> xv(xp, xp + count);
    std::vector<double> yv(yp, yp + count);
    matplot::scatter(xv, yv);
    auto px = matplot::linspace(-20, 20, 100);
    auto py = matplot::transform(px, [&line](auto a) {
        return line->forward(torch::tensor({a, std::pow(a, 2), std::pow(a, 3)}, torch::kFloat32)).item<float>();
    });
    auto plot = matplot::plot(px, py);
    plot->line_width(2);
    matplot::show();
    matplot::cla();
}

int main() {
    const float lr = 0.001F;
    const size_t batch_size  = 10;
    const size_t epoch_count = 16;
    // const size_t epoch_count = 64;
    // const size_t epoch_count = 128;
    std::mt19937 random{std::random_device{}()};
    std::normal_distribution<> normal{4, 1};
    // std::normal_distribution<int> normal{4, 1};
    // x * 1.2 + x ^ 2 * 5.6 + x ^ 3 * 9.6 + 10 + random
    const int count = 100;
    // const int count = 1000;
    // 不计算degree
    auto x = torch::randn({count, 1}) * 5;
    auto features = torch::cat({x, x.pow(2), x.pow(3)}, 1);
    auto labels = features.select(1, 0) * 6.2 + features.select(1, 1) * 3.6 + features.select(1, 2) * -1.6 + 10 + (torch::randn({count}) * 40);
    // auto labels = features.select(1, 0) * 6.2 + features.select(1, 1) * 3.6 + features.select(1, 2) * 1.6 + 10 + (torch::randn({count}) * 40);
    // auto labels = features.select(1, 0) * 6.2 + features.select(1, 1) * -3.6 + features.select(1, 2) * 1.6 + 10 + (torch::randn({count}) * 40);
    std::cout << features.sizes() << '\n';
    std::cout << labels.sizes()   << '\n';
    std::cout << features[0]      << '\n';
    std::cout << labels[0]        << '\n';
    MultiLine line{};
    // torch::nn::L1Loss loss{};
    // torch::nn::MSELoss loss{};
    torch::nn::SmoothL1Loss loss{};
    torch::optim::SGD optimizer{line->parameters(), lr};
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
        std::printf("epoch : %lld | loss : %f\n", epoch, ttLoss * 10 / count);
        linePlot(x, labels, count, line);
    }
    auto names = line->named_parameters();
    auto iterator = names.begin();
    auto end      = names.end();
    for(; iterator != end; ++iterator) {
        std::cout << iterator->key() << " = " << iterator->value() << '\n';
    }
    linePlot(x, labels, count, line);
    // torch::save(line, "D:/tmp/x.pt");
    return 0;
}
