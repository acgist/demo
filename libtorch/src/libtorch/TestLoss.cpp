#include "../header/LibTorch.hpp"

#include "torch/torch.h"
#include "torch/script.h"

static void testL1Loss() {
    // torch::Tensor a = torch::ones({5, 1});
    // torch::Tensor b = torch::ones({5, 1});
    float aa[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    float bb[] = { 2.0F, 4.0F, 3.0F, 4.0F, 5.0F };
    // float bb[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    auto a = torch::from_blob(aa, { 5, 1 }, torch::kFloat32);
    auto b = torch::from_blob(bb, { 5, 1 }, torch::kFloat32);
    // auto a = torch::from_blob(aa, { 1, 5 }, torch::kFloat32);
    // auto b = torch::from_blob(bb, { 1, 5 }, torch::kFloat32);
    std::cout << a;
    std::cout << b;
    std::cout << "\n====\n";
    std::cout << torch::l1_loss(a, b);
}

static void testBCELoss() {
    // torch::Tensor a = torch::ones({5, 1});
    // torch::Tensor b = torch::ones({5, 1});
    float aa[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    // float bb[] = { 2.0F, 4.0F, 3.0F, 4.0F, 5.0F };
    float bb[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    auto a = torch::from_blob(aa, { 5, 1 }, torch::kFloat32);
    auto b = torch::from_blob(bb, { 5, 1 }, torch::kFloat32);
    // auto a = torch::from_blob(aa, { 1, 5 }, torch::kFloat32);
    // auto b = torch::from_blob(bb, { 1, 5 }, torch::kFloat32);
    std::cout << torch::sigmoid(a);
    std::cout << b;
    std::cout << "\n====\n";
    torch::nn::BCELoss loss;
    std::cout << loss(torch::sigmoid(a), b);
}

static void testMSELoss() {
    // torch::Tensor a = torch::ones({5, 1});
    // torch::Tensor b = torch::ones({5, 1});
    float aa[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    // float bb[] = { 2.0F, 4.0F, 3.0F, 4.0F, 5.0F };
    float bb[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    auto a = torch::from_blob(aa, { 5, 1 }, torch::kFloat32);
    auto b = torch::from_blob(bb, { 5, 1 }, torch::kFloat32);
    // auto a = torch::from_blob(aa, { 1, 5 }, torch::kFloat32);
    // auto b = torch::from_blob(bb, { 1, 5 }, torch::kFloat32);
    std::cout << a;
    std::cout << b;
    std::cout << "\n====\n";
    std::cout << torch::mse_loss(a, b);
}

static void testCrossEntropyLoss() {
    // torch::Tensor a = torch::ones({5, 1});
    // torch::Tensor b = torch::ones({5, 1});
    float aa[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    float bb[] = { 1.0F, 2.0F, 3.0F, 4.0F, 5.0F };
    auto a = torch::from_blob(aa, { 5, 1 }, torch::kFloat32);
    auto b = torch::from_blob(bb, { 5, 1 }, torch::kFloat32);
    std::cout << a;
    std::cout << b;
    std::cout << "\n====\n";
    std::cout << torch::cross_entropy_loss(a, b);
}

int main(const int argc, const char * const argv[]) {
    // testL1Loss();
    testBCELoss();
    // testMSELoss();
    // testCrossEntropyLoss();
    return 0;
}
