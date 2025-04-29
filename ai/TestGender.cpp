#include <memory>
#include <random>
#include <filesystem>

#include "torch/nn.h"
#include "torch/optim.h"

#include "opencv2/opencv.hpp"

#include "Dataset.hpp"

// #define LOSS_NLL

class GenderModuleImpl : public torch::nn::Module {

public:
    torch::nn::Sequential feature { nullptr }; // 卷积池化块
    torch::nn::Sequential classify{ nullptr }; // 分类块

public:
    GenderModuleImpl() {
        // 卷积池化块
        torch::nn::Sequential feature;
        feature->push_back(torch::nn::BatchNorm2d(3));
        feature->push_back(torch::nn::Conv2d(torch::nn::Conv2dOptions(3, 16, 3)));
        feature->push_back(torch::nn::MaxPool2d(torch::nn::MaxPool2dOptions(3)));
        feature->push_back(torch::nn::BatchNorm2d(16));
        feature->push_back(torch::nn::Conv2d(torch::nn::Conv2dOptions(16, 32, 3)));
        feature->push_back(torch::nn::MaxPool2d(torch::nn::MaxPool2dOptions(3)));
        this->feature = this->register_module("feature", feature);
        // 分类块
        torch::nn::Sequential classify;
        classify->push_back(torch::nn::ReLU(torch::nn::ReLUOptions(true)));
        // classify->push_back(torch::nn::Dropout()); // 避免过拟合但是收敛很慢
        classify->push_back(torch::nn::Linear(torch::nn::LinearOptions(32 * 8 * 5, 256)));
        classify->push_back(torch::nn::Linear(torch::nn::LinearOptions(256, 2)));
        this->classify = this->register_module("classify", classify);
    }
    torch::Tensor forward(torch::Tensor x) {
        x = this->feature->forward(x);
        x = this->classify->forward(x.flatten(1));
        #ifdef LOSS_NLL
        return torch::log_softmax(x, 1);
        #else
        return x;
        #endif
    }
    virtual ~GenderModuleImpl() {
        this->unregister_module("feature");
        this->unregister_module("classify");
    }

};

TORCH_MODULE(GenderModule);

inline torch::Tensor read_image(const std::string& path, const int width = 60, const int height = 80) {
    cv::Mat image = cv::imread(path);
    const int cols = image.cols;
    const int rows = image.rows;
    const double ws = 1.0 * cols / width;
    const double hs = 1.0 * rows / height;
    const double scale = std::max(ws, hs);
    const int w = std::max(static_cast<int>(width  * scale), cols);
    const int h = std::max(static_cast<int>(height * scale), rows);
    cv::Mat result = cv::Mat::zeros(h, w, CV_8UC3);
    image.copyTo(result(cv::Rect(0, 0, cols, rows)));
    cv::resize(result, image, cv::Size(width, height));
    // cv::imshow("image", image);
    // cv::waitKey();
    return torch::from_blob(image.data, { image.rows, image.cols, 3 }, torch::kByte).permute({2, 0, 1}).to(torch::kFloat32).div(255.0).clone();
}

int main() {
    try {
    const float lr        = 0.01F;
    const int batch_size  = 100;
    const int epoch_count = 32;
    // 数据
    std::vector<torch::Tensor> labels;
    std::vector<torch::Tensor> features;
    auto iterator = std::filesystem::directory_iterator(std::filesystem::path("D:\\tmp\\gender\\val\\man"));
    for(const auto& entry : iterator) {
        const auto entry_path = entry.path();
        auto tensor = read_image(entry_path.string());
        #ifdef LOSS_NLL
        labels  .push_back(torch::tensor({ 1 }, torch::kLong));
        #else
        labels  .push_back(torch::tensor({ 0, 1 }, torch::kFloat32));
        #endif
        features.push_back(std::move(tensor));
        std::cout << "加载文件：" << entry_path << std::endl;
    }
    iterator = std::filesystem::directory_iterator(std::filesystem::path("D:\\tmp\\gender\\val\\woman"));
    for(const auto& entry : iterator) {
        const auto entry_path = entry.path();
        auto tensor = read_image(entry_path.string());
        #ifdef LOSS_NLL
        labels  .push_back(torch::tensor({ 0 }, torch::kLong));
        #else
        labels  .push_back(torch::tensor({ 1, 0 }, torch::kFloat32));
        #endif
        features.push_back(std::move(tensor));
        std::cout << "加载文件：" << entry_path << std::endl;
    }
    auto dataset = Dataset(labels, features).map(torch::data::transforms::Stack<>());
    auto loader  = torch::data::make_data_loader<LFT_RND_SAMPLER>(std::move(dataset), batch_size);
    // 训练
    GenderModule gender{};
    #ifdef LOSS_NLL
    auto loss = torch::nn::NLLLoss{};
    #else
    auto loss = torch::nn::CrossEntropyLoss{};
    #endif
    auto optimizer = torch::optim::Adam{gender->parameters(), lr};
    for(int epoch = 0; epoch < epoch_count; ++epoch) {
        float ttLoss = 0.0F;
        size_t accu_val = 0;
        size_t data_val = 0;
        auto confusion_matrix = torch::zeros({ 2, 2 }, torch::kInt).requires_grad_(false);
        for(const auto& x : *loader) {
            auto output = gender->forward(x.data);
            #ifdef LOSS_NLL
            auto tLoss  = loss(output, x.target.flatten());
            #else
            auto tLoss  = loss(output, x.target);
            #endif
            optimizer.zero_grad();
            tLoss.backward();
            optimizer.step();
            ttLoss += tLoss.item<float>();
            // 计算混淆矩阵（无关训练）
            {
                torch::NoGradGuard no_grad_guard;
                #ifdef LOSS_NLL
                auto target_index = x.target;
                auto pred_index   = output.argmax(1);
                auto batch_size   = pred_index.numel();
                #else
                auto target_index = x.target.argmax(1);
                auto pred_index   = torch::softmax(output, 1).argmax(1);
                auto batch_size   = pred_index.numel();
                #endif
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
    gender->eval();
    torch::Tensor tensor = read_image("D:/tmp/lcw.png").unsqueeze(0);
    #ifdef LOSS_NLL
    auto pred = gender->forward(tensor);
    #else
    auto pred = torch::softmax(gender->forward(tensor), 1);
    #endif
    std::cout << "预测结果\n" << pred << std::endl;
    std::cout << "预测类别\n" << pred.argmax(1) << std::endl;
    std::cout << "预测类别\n" << ((pred.argmax(1).item<int>() == 0) ? "女" : "男") << std::endl;
    tensor = read_image("D:/tmp/lyf.png").unsqueeze(0);
    #ifdef LOSS_NLL
    pred = gender->forward(tensor);
    #else
    pred = torch::softmax(gender->forward(tensor), 1);
    #endif
    std::cout << "预测结果\n" << pred << std::endl;
    std::cout << "预测类别\n" << pred.argmax(1) << std::endl;
    std::cout << "预测类别\n" << ((pred.argmax(1).item<int>() == 0) ? "女" : "男") << std::endl;
    tensor = read_image("D:/tmp/mm.png").unsqueeze(0);
    #ifdef LOSS_NLL
    pred = gender->forward(tensor);
    #else
    pred = torch::softmax(gender->forward(tensor), 1);
    #endif
    std::cout << "预测结果\n" << pred << std::endl;
    std::cout << "预测类别\n" << pred.argmax(1) << std::endl;
    std::cout << "预测类别\n" << ((pred.argmax(1).item<int>() == 0) ? "女" : "男") << std::endl;
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
    return 0;
}
