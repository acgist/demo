#include <random>
#include <vector>
#include <iostream>
#include <algorithm>

#include "torch/torch.h"
#include "torch/script.h"

static torch::Tensor model(torch::Tensor& x, torch::Tensor& w, torch::Tensor& b) {
    // std::cout << "x = " << x.sizes() << "\n";
    // std::cout << "w = " << w.sizes() << "\n";
    // std::cout << "b = " << b.sizes() << "\n";
    // return torch::mm(x, w) + b;
    return torch::matmul(x, w) + b;
}

static torch::Tensor loss(torch::Tensor& p, torch::Tensor& y) {
    // std::cout << "p = " << p << "\n";
    // std::cout << "y = " << y << "\n";
    // std::cout << "p = " << p.sizes() << "\n";
    // std::cout << "y = " << y.sizes() << "\n";
    return (p - y.view(p.sizes())).pow(2) / 2;
}

static void sgd(torch::Tensor& w, torch::Tensor& b, const float& lr, const size_t& batch_size) {
    w.data() -= w.grad() * (lr / batch_size);
    b.data() -= b.grad() * (lr / batch_size);
}

int main() {
    std::random_device device;
    std::mt19937 rand(device());
    std::normal_distribution<> normal(100, 10);
    torch::Tensor features = torch::randn({ 1000, 2 });
    // 5.8 * a + 87.4 * b + 1024 + rand = c
    torch::Tensor labels = features.select(1, 0) * 5.8 + features.select(1, 1) * 87.4 + 1024 + normal(rand);
    // std::cout << labels   << "\n";
    // std::cout << features << "\n";
    // std::cout << labels[999]   << "\n";
    // std::cout << features[999] << "\n";
    std::cout << labels.sizes()   << "\n";
    std::cout << features.sizes() << "\n";
    std::vector<long long> index(1000);
    // std::vector<long long> index{1000};
    // index.reserve(1000);
    for(long long i = 0; i < 1000; ++i) {
        index.push_back(i);
    }
    try {
        const int epochs = 100;
        const int batch_size = 10;
        torch::Tensor w = torch::normal(0, 0.01, {2, 1});
        // torch::Tensor w = torch::zeros({2, 1}, torch::kFloat16);
        w.set_requires_grad(true);
        torch::Tensor b = torch::zeros({1}, torch::kFloat16);
        b.set_requires_grad(true);
        for(long long epoch = 0; epoch < epochs; ++epoch) {
            std::shuffle(index.begin(), index.end(), rand);
            for(auto iterator = index.begin(); iterator != index.end(); iterator += batch_size) {
                long long indexs[batch_size];
                std::copy(iterator, iterator + batch_size, indexs);
                // std::cout << "index = " << torch::from_blob(indexs, {batch_size}, torch::kLong) << "\n";
                auto tLoss = loss(
                    model(features.index_select(0, torch::from_blob(indexs, {batch_size}, torch::kLong)), w, b),
                    labels.index_select(0, torch::from_blob(indexs, {batch_size}, torch::kLong))
                ).sum();
                tLoss.backward();
                sgd(w, b, 0.03, batch_size);
                w.grad().data().zero_();
                b.grad().data().zero_();
                // printf("epoch = %lld | batch = %lld | loss = %f\n", epoch, iterator - index.begin(), tLoss.mean().item<float>());
            }
            {
                torch::NoGradGuard guard;
                torch::Tensor vLoss = loss(model(features, w, b), labels);
                printf("epoch = %lld | loss = %f\n", epoch, vLoss.mean().item<float>());
            }
        }
        std::cout << "w = " << w << " | b = " << b << "\n";
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
    return 0;
}