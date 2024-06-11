#include "../header/LibTorch.hpp"

#include <chrono>
#include <iostream>
#include <filesystem>

#include "torch/torch.h"
#include "torch/script.h"

#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"

#include "../header/Layers.hpp"
#include "../header/Datasets.hpp"

/**
 * 性别模型（参考VGG）
 * 设置不要计算梯度：层.value().set_requires_grad()
 */
class GenderImpl : public torch::nn::Module {

private:
    // 卷积层
    torch::nn::Sequential        features{ nullptr };
    // 池化层
    torch::nn::AdaptiveAvgPool2d avgPool{ nullptr };
    // 全连接层
    torch::nn::Sequential        classifier{ nullptr };

public:
    GenderImpl(int num_classes = 2);
    torch::Tensor forward(torch::Tensor x);

};
TORCH_MODULE(Gender);

class GenderHandler {

public:
    torch::Device device = torch::Device(torch::kCPU);
    Gender model{ nullptr };

public:
    // 加载模型
    void load(const std::string& modelPath);
    // // 训练模型
    void trian(
        size_t epoch,
        size_t batch_size,
        torch::optim::Optimizer& optimizer,
        lifuren::datasets::ImageFileDataset& dataset
    );
    // // 验证模型
    void val(
        size_t epoch,
        size_t batch_size,
        lifuren::datasets::ImageFileDataset& dataset
    );
    // // 测试模型
    void test(
        const std::string& data_dir,
        const std::string& image_type
    );
    // 训练验证
    virtual void trainAndVal(
        size_t num_epochs,
        size_t batch_size,
        float  learning_rate,
        const  std::string& data_dir,
        const  std::string& image_type,
        const  std::string& save_path
    );
    // 模型预测
    int pred(cv::Mat& image);

};

/**
 * @see #conv2d
 * @see #batchNorm2d
 * @see #relu
 */
inline void conv2dBatchNorm2dRelu(
    torch::nn::Sequential sequential,
    int64_t in_channels,
    int64_t out_channels,
    int64_t kernel_size,
    int64_t stride   = 1,
    int64_t padding  = 0,
    int64_t dilation = 1,
    bool    bias     = true,
    bool    inplace  = false
) {
    sequential->push_back(lifuren::layers::conv2d(in_channels, out_channels, kernel_size, stride, padding, dilation, bias));
    sequential->push_back(lifuren::layers::batchNorm2d(out_channels));
    sequential->push_back(lifuren::layers::relu(inplace));
}

GenderImpl::GenderImpl(int num_classes){
    // // 卷积
    // torch::nn::Sequential features;
    // conv2dBatchNorm2dRelu(features, 3, 64, 3, 1, 1);
    // conv2dBatchNorm2dRelu(features, 64, 64, 3, 1, 1);
    // features->push_back(lifuren::layers::maxPool2d(2, 2));
    // conv2dBatchNorm2dRelu(features, 64, 128, 3, 1, 1);
    // conv2dBatchNorm2dRelu(features, 128, 128, 3, 1, 1);
    // features->push_back(lifuren::layers::maxPool2d(2, 2));
    // conv2dBatchNorm2dRelu(features, 128, 256, 3, 1, 1);
    // conv2dBatchNorm2dRelu(features, 256, 256, 3, 1, 1);
    // features->push_back(lifuren::layers::maxPool2d(2, 2));
    // this->features = register_module("features", features);
    // // 池化
    // this->avgPool = lifuren::layers::adaptiveAvgPool2d(32);
    // // 分类
    // torch::nn::Sequential classifier;
    // classifier->push_back(torch::nn::Linear(torch::nn::LinearOptions(256 * 32 * 32, 1024)));
    // classifier->push_back(torch::nn::ReLU(torch::nn::ReLUOptions(true)));
    // classifier->push_back(torch::nn::Dropout());
    // classifier->push_back(torch::nn::Linear(torch::nn::LinearOptions(1024, 512)));
    // classifier->push_back(torch::nn::ReLU(torch::nn::ReLUOptions(true)));
    // classifier->push_back(torch::nn::Dropout());
    // classifier->push_back(torch::nn::Linear(torch::nn::LinearOptions(512, 256)));
    // classifier->push_back(torch::nn::ReLU(torch::nn::ReLUOptions(true)));
    // classifier->push_back(torch::nn::Dropout());
    // classifier->push_back(torch::nn::Linear(torch::nn::LinearOptions(256, num_classes)));
    // this->classifier = register_module("classifier", classifier);
    // 小参快速测试
    // 卷积
    torch::nn::Sequential features;
    conv2dBatchNorm2dRelu(features, 3, 8, 3, 1, 1);
    conv2dBatchNorm2dRelu(features, 8, 16, 3, 1, 1);
    features->push_back(lifuren::layers::maxPool2d(2, 2));
    conv2dBatchNorm2dRelu(features, 16, 16, 3, 1, 1);
    conv2dBatchNorm2dRelu(features, 16, 16, 3, 1, 1);
    features->push_back(lifuren::layers::maxPool2d(2, 2));
    this->features = register_module("features", features);
    // 池化
    this->avgPool = lifuren::layers::adaptiveAvgPool2d(32);
    // 分类
    torch::nn::Sequential classifier;
    classifier->push_back(torch::nn::Linear(torch::nn::LinearOptions(16 * 32 * 32, 1024)));
    classifier->push_back(torch::nn::ReLU(torch::nn::ReLUOptions(true)));
    classifier->push_back(torch::nn::Dropout());
    classifier->push_back(torch::nn::Linear(torch::nn::LinearOptions(1024, 256)));
    classifier->push_back(torch::nn::ReLU(torch::nn::ReLUOptions(true)));
    classifier->push_back(torch::nn::Dropout());
    classifier->push_back(torch::nn::Linear(torch::nn::LinearOptions(256, num_classes)));
    this->classifier = register_module("classifier", classifier);
}

torch::Tensor GenderImpl::forward(torch::Tensor x) {
    x = this->features->forward(x);
    x = this->avgPool->forward(x);
    x = torch::flatten(x, 1);
    x = this->classifier->forward(x);
    return torch::log_softmax(x, 1);
}

void GenderHandler::trainAndVal(
    size_t num_epochs,
    size_t batch_size,
    float  learning_rate,
    const  std::string& data_dir,
    const  std::string& image_type,
    const  std::string& save_path
) {
    printf("是否使用CUDA：%d", this->device.is_cuda());
    std::filesystem::path data_path = data_dir;
    std::string path_val   = (data_path / "val").u8string();
    std::string path_train = (data_path / "train").u8string();
    std::map<std::string, int> mapping = {
        { "man"  , 1 },
        { "woman", 0 }
    };
    this->model->to(this->device);
    auto data_loader_val   = lifuren::datasets::loadImageFileDataset(200, 200, batch_size, path_val,   image_type, mapping);
    auto data_loader_train = lifuren::datasets::loadImageFileDataset(200, 200, batch_size, path_train, image_type, mapping);
    for (size_t epoch = 1; epoch <= num_epochs; ++epoch) {
        if (epoch == num_epochs / 4) {
            learning_rate /= 10;
        }
        try {
            torch::optim::Adam optimizer(this->model->parameters(), learning_rate);
            auto a = std::chrono::system_clock::now();
            this->trian(epoch, batch_size, optimizer, data_loader_train);
            auto z = std::chrono::system_clock::now();
            printf("训练耗时：%lld", std::chrono::duration_cast<std::chrono::milliseconds>((z - a)).count());
            this->val(epoch, batch_size, data_loader_val);
        } catch(const std::exception& e) {
            printf("训练异常：%s", e.what());
        }
    }
    torch::save(this->model, save_path);
}

void GenderHandler::trian(
    size_t epoch,
    size_t batch_size,
    torch::optim::Optimizer& optimizer,
    lifuren::datasets::ImageFileDataset& dataset
) {
    float acc_train  = 0.0;
    float loss_train = 0.0;
    size_t batch_index = 0;
    this->model->train();
    for (auto& batch : *dataset) {
        auto data   = batch.data.to(this->device).to(torch::kF32).div(255.0);
        auto target = batch.target.to(this->device).squeeze().to(torch::kInt64);
        optimizer.zero_grad();
        torch::Tensor prediction = this->model->forward(data);
        torch::Tensor loss = torch::nll_loss(prediction, target);
        loss.backward();
        optimizer.step();
        auto acc = prediction.argmax(1).eq(target).sum();
        acc_train  += acc.item<float>();
        loss_train += loss.item<float>();
        batch_index++;
        std::printf("Epoch: %zd - %zd | Train Loss: %.8f - %.8f | Train Acc: %.8f \r", epoch, batch_index, loss.item<float>(), loss_train / batch_index, acc_train / batch_size / batch_index);
        std::flush(std::cout);
    }
    std::cout << std::endl;
}

void GenderHandler::val(
    size_t epoch,
    size_t batch_size,
    lifuren::datasets::ImageFileDataset& dataset
) {
    float acc_val  = 0.0;
    float loss_val = 0.0;
    size_t batch_index = 0;
    this->model->eval();
    for (auto& batch : *dataset) {
        auto data   = batch.data.to(this->device).to(torch::kF32).div(255.0);
        auto target = batch.target.to(this->device).squeeze().to(torch::kInt64);
        torch::Tensor prediction = this->model->forward(data);
        torch::Tensor loss = torch::nll_loss(prediction, target);
        auto acc = prediction.argmax(1).eq(target).sum();
        acc_val  += acc.template item<float>();
        loss_val += loss.template item<float>();
        batch_index++;
        std::printf("Epoch: %zd - %zd | Val Loss: %.8f - %.8f | Val Acc: %.8f \r", epoch, batch_index, loss.item<float>(), loss_val / batch_index, acc_val / batch_size / batch_index);
        std::flush(std::cout);
    }
    std::cout << std::endl;
}

void GenderHandler::test(
    const std::string& data_dir,
    const std::string& image_type
) {
    // 没有测试
}

void GenderHandler::load(const std::string& modelPath) {
    torch::load(this->model, modelPath);
}

int GenderHandler::pred(cv::Mat& image) {
    cv::resize(image, image, cv::Size(200, 200));
    torch::Tensor image_tensor = torch::from_blob(image.data, { image.rows, image.cols, 3 }, torch::kByte).permute({ 2, 0, 1 });
    image_tensor = image_tensor.to(this->device).unsqueeze(0).to(torch::kF32).div(255.0);
    auto prediction = this->model->forward(image_tensor);
    prediction = torch::softmax(prediction, 1);
    std::cout << "预测结果：" << prediction << "\n";
    auto class_id = prediction.argmax(1);
    int class_val = class_id.item<int>();
    printf("预测结果：%d - %f", class_id.item().toInt(), prediction[0][class_val].item().toFloat());
    return class_val;
}

int main(const int argc, const char * const argv[]) {
    Gender gender;
    GenderHandler handler;
    handler.model = gender;
    if(torch::cuda::is_available()) {
        handler.device = torch::Device(torch::kCUDA);
    }
    #ifdef _WIN32
    handler.trainAndVal(
        32,
        8,
        0.001,
        "D:\\tmp\\gender",
        ".jpg",
        "D:\\tmp\\gender\\model.pt"
    );
    // handler.load("D:\\tmp\\gender\\model.pt");
    cv::Mat image = cv::imread("D:\\tmp\\yusheng.jpg");
    printf("预测结果：%d", handler.pred(image));
    image.release();
    image = cv::imread("D:\\tmp\\girl.png");
    printf("预测结果：%d", handler.pred(image));
    image.release();
    #else
    handler.trainAndVal(
        32,
        8,
        0.001,
        "/tmp/gender",
        ".jpg",
        "/tmp/gender/model.pt"
    );
    #endif
    return 0;
}
