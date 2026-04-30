/**
 * https://blog.51cto.com/u_16213402/7416625
 * https://blog.csdn.net/a1314_521a/article/details/124159489
 * https://blog.csdn.net/windhawk_fly/article/details/132598043
 * https://blog.csdn.net/fgg1234567890/article/details/115261293
 */

#include "torch/torch.h"

class PoetryImpl : public torch::nn::Module {

public:
    torch::nn::Embedding embedding{nullptr};
    torch::nn::GRU gru{nullptr};
    torch::nn::Linear linear{nullptr};
    torch::nn::Softmax softmax{nullptr};

};

TORCH_MODULE(Poetry);

int main() {
    
    return 0;
}