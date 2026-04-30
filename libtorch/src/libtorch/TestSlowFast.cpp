#include <iostream>

/**
 * mmaction2 = python=3.11
 * 
 * https://github.com/open-mmlab/mmaction2/blob/main/docs/zh_cn/user_guides/config.md
 * 
 * python ../demo/demo.py tsn_imagenet-pretrained-r50_8xb32-1x1x8-100e_kinetics400-rgb.py tsn_imagenet-pretrained-r50_8xb32-1x1x8-100e_kinetics400-rgb_20220906-2692d16c.pth ../demo/demo.mp4 ../tools/data/kinetics/label_map_k400.txt
 * python ../demo/long_video_demo.py --device cuda ../demo/demo_configs/tsn_r50_1x1x8_video_infer.py tsn_r50_1x1x3_100e_kinetics400_rgb_20200614-e508be42.pth ./video.mp4 ../tools/data/kinetics/label_map_k400.txt output.mp4
 * 
 * https://download.openmmlab.com/mmaction/recognition/tsn/tsn_r50_1x1x3_100e_kinetics400_rgb/tsn_r50_1x1x3_100e_kinetics400_rgb_20200614-e508be42.pth
 */

#include "torch/torch.h"
#include "torch/script.h"
#include "opencv2/opencv.hpp"

int main() {
    auto model = torch::jit::load("D:/tmp/video_model/tsn_imagenet-pretrained-r50_8xb32-1x1x8-100e_kinetics400-rgb_20220906-2692d16c.pth");
    std::vector<torch::jit::IValue> inputs;
    auto input = torch::randn({ 1 });
    inputs.push_back(std::move(input));
    model.eval();
    auto tensor = model.forward(inputs);
    auto result = tensor.toTensor().template item<float>();
    std::cout << "识别结果：" << result << '\n';
    return 0;
}