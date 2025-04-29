#include <random>
#include <vector>
#include <iostream>

#include "torch/torch.h"
#include "torch/script.h"

#include "matplot/matplot.h"

#include "Dataset.hpp"

class ClassifyModuleImpl : public torch::nn::Module {

private:
    torch::nn::BatchNorm1d norm{ nullptr };
    torch::nn::Linear linear1  { nullptr };
    torch::nn::Linear linear2  { nullptr };

public:
    ClassifyModuleImpl() {
        this->norm    = this->register_module("norm",    torch::nn::BatchNorm1d(2));
        // this->linear1 = this->register_module("linear1", torch::nn::Linear(torch::nn::LinearOptions( 2, 4)));
        this->linear1 = this->register_module("linear1", torch::nn::Linear(torch::nn::LinearOptions( 2, 16)));
        this->linear2 = this->register_module("linear2", torch::nn::Linear(torch::nn::LinearOptions(16,  4)));
    }
    torch::Tensor forward(torch::Tensor input) {
        // 单线性层泛化较差
        // auto output = this->linear1(input);
        // 归一化很重要
        // auto output = this->linear1(input);
        auto output = this->linear1(this->norm(input));
             output = this->linear2(torch::relu(output));
        return output;
        // return torch::softmax(output, 1);
    }
    virtual ~ClassifyModuleImpl() {
        this->unregister_module("norm");
        this->unregister_module("linear1");
        this->unregister_module("linear2");
    }

};

TORCH_MODULE(ClassifyModule);

int main() {
    const float lr         = 0.01F;
    const int batch_size   = 100; // 合适的大小：10收敛很慢
    const int epoch_count  = 8;
    const int dataset_size = 4000;
    // 数据
    std::mt19937 rand(std::random_device{}());
    std::normal_distribution<float> w(10.0, 1.0); // 标准差越大越难拟合
    std::normal_distribution<float> b( 0.5, 0.2);
    std::vector<torch::Tensor> labels;
    std::vector<torch::Tensor> features;
    labels  .reserve(dataset_size);
    features.reserve(dataset_size);
    std::vector<double> x;
    std::vector<double> y;
    x.reserve(dataset_size);
    y.reserve(dataset_size);
    for(int index = 0; index < dataset_size; ++index) {
        int label = index % 4;
        float l[] = { 0, 0, 0, 0 };
        float f[] = { w(rand) * label + b(rand), w(rand) * label + b(rand) };
        l[label]  = 1.0F;
        labels  .push_back(torch::from_blob(l, { 4 }, torch::kFloat32).clone());
        features.push_back(torch::from_blob(f, { 2 }, torch::kFloat32).clone());
        x.push_back(f[0]);
        y.push_back(f[1]);
    }
    matplot::hold(true);
    matplot::scatter(x, y);
    matplot::show();
    auto dataset = Dataset(labels, features).map(torch::data::transforms::Stack<>());
    auto loader  = torch::data::make_data_loader<LFT_RND_SAMPLER>(std::move(dataset), batch_size);
    // 训练
    ClassifyModule classify{};
    auto loss = torch::nn::CrossEntropyLoss{};
    auto optimizer = torch::optim::Adam{classify->parameters(), lr};
    for(int epoch = 0; epoch < epoch_count; ++epoch) {
        float ttLoss = 0.0F;
        size_t accu_val = 0;
        size_t data_val = 0;
        auto confusion_matrix = torch::zeros({ 4, 4 }, torch::kInt).requires_grad_(false);
        for(auto& x : *loader) {
            auto output = classify->forward(x.data);
            auto tLoss  = loss(output, x.target);
            optimizer.zero_grad();
            tLoss.backward();
            optimizer.step();
            ttLoss += tLoss.item<float>();
            // 计算混淆矩阵（无关训练）
            {
                torch::NoGradGuard no_grad_guard;
                auto target_index = x.target.argmax(1).to(torch::kCPU);
                auto pred_index   = torch::log_softmax(output, 1).argmax(1).to(torch::kCPU);
                auto batch_size   = pred_index.numel();
                auto accu = pred_index.eq(target_index).sum();
                accu_val += accu.template item<int>();
                data_val += batch_size;
                int64_t* target_index_iter = target_index.data_ptr<int64_t>();
                int64_t* pred_index_iter   = pred_index.data_ptr<int64_t>();
                for (int64_t i = 0; i < batch_size; ++i, ++target_index_iter, ++pred_index_iter) {
                    confusion_matrix[*target_index_iter][*pred_index_iter].add_(1);
                }
            }
        }
        std::cout << "混淆矩阵：\n" << confusion_matrix << std::endl;
    }
    // 预测
    classify->eval();
    std::vector<float> data = {
        0.1F,   0.2F,
        2.0F,   1.0F,
        10.0F, 11.0F,
        20.0F, 22.0F,
        30.0F, 33.0F,
        90.0F, 99.0F,
    };
    auto pred = torch::log_softmax(classify->forward(torch::from_blob(data.data(), { static_cast<int>(data.size()) / 2, 2 }, torch::kFloat32)), 1);
    std::cout << "预测结果\n" << pred << std::endl;
    std::cout << "预测类别\n" << pred.argmax(1) << std::endl;
    // 绘图
    x.clear();
    y.clear();
    for(auto iter = data.begin(); iter != data.end();) {
        x.push_back(*iter);
        ++iter;
        y.push_back(*iter);
        ++iter;
    }
    matplot::scatter(x, y);
    matplot::show();
    // 绘图
    std::vector<double> x1;
    std::vector<double> y1;
    std::vector<double> x2;
    std::vector<double> y2;
    std::vector<double> x3;
    std::vector<double> y3;
    std::vector<double> x4;
    std::vector<double> y4;
    for(int i = 0; i < 100; i += 2) {
    for(int j = 0; j < 100; j += 2) {
        pred = torch::log_softmax(classify->forward(torch::tensor({i, j}, torch::kFloat32).reshape({1, 2})), 1);
        int value = pred.argmax(1).item<int>();
        switch(value) {
            case 1:
            x1.push_back(i);
            y1.push_back(j);
            break;
            case 2:
            x2.push_back(i);
            y2.push_back(j);
            break;
            case 3:
            x3.push_back(i);
            y3.push_back(j);
            break;
            case 4:
            x4.push_back(i);
            y4.push_back(j);
            break;
        }
    }
    }
    matplot::scatter(x1, y1);
    matplot::scatter(x2, y2);
    matplot::scatter(x3, y3);
    matplot::scatter(x4, y4);
    matplot::show();
    return 0;
}
