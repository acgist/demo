#include "torch/torch.h"

#include <iostream>

static void testPermute() {
    auto t = torch::rand({ 3, 2 });
    std::cout << t << '\n';
    t = t.permute({ 1, 0 });
    std::cout << t << '\n';
}

int main() {
    testPermute();
    return 0;
}
