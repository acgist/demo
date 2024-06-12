#include <random>
#include <vector>
#include <iostream>
#include <algorithm>

#include "torch/torch.h"
#include "torch/script.h"

class Price {

public:
    float a;
    float b;
    float c;

};

static const int MAX = 10000;
static Price prices[MAX];

// 太落伍了
static void initFeature() {
    std::random_device device;
    std::mt19937 rand(device());
    std::normal_distribution<> normal(100, 10);
    std::uniform_int_distribution<> uniform(500, 1000);
    for(int i = 0; i < MAX; ++i) {
        auto& x = prices[i];
        x.a = uniform(rand);
        x.b = uniform(rand);
        // 5.8 * a + 87.4 * b + 1024 + rand = c
        x.c = x.a * 5.8 + x.b * 87.4 + 1024 + normal(rand);
    }
}

static torch::Tensor model(torch::Tensor& x, torch::Tensor& w, torch::Tensor& b) {
    return torch::mm(x, w) + b;
}

static torch::Tensor loss(torch::Tensor& p, torch::Tensor& y) {
    return (p - y.view(p.sizes())).pow(2) / 2;
}

static void sgd(torch::Tensor& w, torch::Tensor& b, const float& lr, const size_t& batch_size) {
    w.data() -= w.grad() * (lr / batch_size);
    b.data() -= b.grad() * (lr / batch_size);
}

int main() {
    // initFeature();
    std::random_device device;
    std::mt19937 rand(device());
    std::normal_distribution<> normal(100, 10);
    torch::Tensor features = torch::randn({ 1000, 2 });
    // 5.8 * a + 87.4 * b + 1024 + rand = c
    torch::Tensor labels = features.select(1, 0) * 5.8 + features.select(1, 1) * 87.4 + 1024 + normal(rand);
    std::cout << features << "\n";
    std::cout << features[999] << "\n";
    std::cout << labels[999]   << "\n";
    std::vector<int> index{1000};
    for(int i = 0; i < 1000; ++i) {
        index.push_back(i);
    }
    std::shuffle(index.begin(), index.end(), rand);
    torch::Tensor w = torch::zeros({2, 0}, torch::kFloat16);
    w.set_requires_grad(true);
    torch::Tensor b = torch::zeros({1}, torch::kFloat16);
    b.set_requires_grad(true);
    for(int epoch = 0; epoch < 3; ++epoch) {
        for(int j = 0; j < 1000; j += 10) {

            // loss(model(),).sum
            // loss.back
            sgd(w, b, 0.03, 10);
            w.grad().data().zero_();
            b.grad().data().zero_();
        }
        {
            torch::NoGradGuard guard;
            torch::Tensor loss;
            printf("epoch = %d | loss = %f", epoch, loss.mean().item<float>());
        }
    }
    return 0;
}