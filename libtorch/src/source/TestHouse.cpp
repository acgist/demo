#include <iostream>

#include "../header/Matplot.hpp"
#include "../header/Datasets.hpp"

class ModelImpl : public torch::nn::Module {

public:
    torch::nn::Sequential linear{ nullptr };

public:
    ModelImpl();

public:
    torch::Tensor forward(torch::Tensor x);

};

TORCH_MODULE(Model);

ModelImpl::ModelImpl() {
    torch::nn::Sequential net{};
    // 单层
    auto linear = torch::nn::Linear{304, 1};
    // torch::nn::init::normal_(linear->weight, 0, 0.01);
    // if(linear->options.bias()) {
    //     torch::nn::init::normal_(linear->bias, 0, 0.01);
    // }
    net->push_back(linear);
    // 多层
    // net->push_back(torch::nn::Linear{304, 128});
    // // net->push_back(torch::nn::BatchNorm1d{100});
    // net->push_back(torch::nn::Dropout{});
    // net->push_back(torch::nn::ReLU{});
    // net->push_back(torch::nn::Linear{128, 64});
    // // net->push_back(torch::nn::BatchNorm1d{100});
    // net->push_back(torch::nn::Dropout{});
    // net->push_back(torch::nn::ReLU{});
    // net->push_back(torch::nn::Linear{64, 1});
    // 网络定义
    this->linear = register_module("linear", net);
    for(auto& m : modules(false)) {
        std::cout << "初始化权重：" << m->name() << '\n';
    }
}

torch::Tensor ModelImpl::forward(torch::Tensor x) {
    return this->linear->forward(x);
    // return this->linear->forward(x);
}

int main() {
    // 数据
    int rows = 0;
    int cols = 0;
    auto csv = lifuren::datasets::loadCSV("D:/tmp/house/train.csv");
    // acgist::plotHist(acgist::selectCols(csv, 1, 1));
    // acgist::plotPie(acgist::countCols(csv, 2, 1));
    // acgist::plotHist(acgist::selectCols(csv, 3, 1));
    // acgist::plotHist(acgist::selectCols(csv, 4, 1));
    auto mark = lifuren::datasets::mark(csv, rows, cols);
    auto tensor = torch::from_blob(mark.data(), { rows, cols }, torch::kFloat);
    std::cout << rows << " " << cols << " " << tensor.sizes() << '\n';
    auto features = tensor.narrow(1, 0, cols - 1);
    auto labels   = tensor.narrow(1, cols - 1, 1);
    // auto features = torch::nn::functional::normalize(tensor.narrow(1, 0, cols - 1), torch::nn::functional::NormalizeFuncOptions{}.dim(0).eps(1e-8));
    // auto labels   = torch::nn::functional::normalize(tensor.narrow(1, cols - 1, 1), torch::nn::functional::NormalizeFuncOptions{}.dim(0).eps(1e-8));
    // std::cout << features[0] << '\n';
    // std::cout << labels[0] << '\n';
    float scale = 1.0F;
    // float scale = 10000.0F;
    // float scale = 100000.0F;
    labels /= scale;
    std::cout << features.sizes() << '\n';
    std::cout << labels.sizes() << '\n';
    // 训练
    // float lr = 0.1F;
    float lr = 0.01F;
    // float lr = 0.001F;
    // float lr = 0.002F;
    // float lr = 0.0001F;
    size_t batch_size = 100;
    // size_t epoch_count = 16;
    // size_t epoch_count = 64;
    // size_t epoch_count = 128;
    size_t epoch_count = 256;
    // size_t epoch_count = 512;
    Model model{};
    // torch::nn::init::normal_(model->parameters(), 0, 0.01);
    torch::optim::SGDOptions options{ lr };
    // options.momentum(0.9);
    // options.weight_decay(0.001);
    torch::optim::SGD optimizer{model->parameters(), options};
    // torch::optim::Adam optimizer{model->parameters(), lr};
    // torch::nn::L1Loss loss{};
    torch::nn::MSELoss loss{};
    auto dataset = lifuren::datasets::TensorDataset(features, labels).map(torch::data::transforms::Stack<>());
    auto loader  = torch::data::make_data_loader<torch::data::samplers::RandomSampler>(dataset, batch_size);
    for(size_t epoch = 0; epoch < epoch_count; ++epoch) {
        double ttLoss = 0.0;
        for(auto& data : *loader) {
            // std::cout << data.data.sizes() << '\n';
            // std::cout << data.target.sizes() << '\n';
            auto output = model->forward(data.data);
            auto tLoss = loss(output, data.target);
            optimizer.zero_grad();
            tLoss.backward();
            optimizer.step();
            ttLoss += tLoss.item<float>();
        }
        std::printf("epoch : %lld | loss : %f\n", epoch, ttLoss * batch_size / rows);
    }
    auto testCSV  = lifuren::datasets::loadCSV("D:/tmp/house/test.csv");
    auto testMark = lifuren::datasets::mark(testCSV, rows, cols, true, true, 1, 1, 0);
    auto testTensor = torch::from_blob(testMark.data(), { rows, cols });
    model->eval();
    std::cout << testTensor.sizes() << '\n';
    // 208500 181500 223500 140000
    std::cout << model->forward(features[0].squeeze(0)).item<float>() * scale << '\n';
    std::cout << model->forward(features[1].squeeze(0)).item<float>() * scale << '\n';
    std::cout << model->forward(features[2].squeeze(0)).item<float>() * scale << '\n';
    std::cout << model->forward(features[3].squeeze(0)).item<float>() * scale << '\n';
    std::cout << "====\n";
    // 169277 187758 183583 179317
    std::cout << model->forward(testTensor[0].squeeze(0)).item<float>() * scale << '\n';
    std::cout << model->forward(testTensor[1].squeeze(0)).item<float>() * scale << '\n';
    std::cout << model->forward(testTensor[2].squeeze(0)).item<float>() * scale << '\n';
    std::cout << model->forward(testTensor[3].squeeze(0)).item<float>() * scale << '\n';
    return 0;
}
