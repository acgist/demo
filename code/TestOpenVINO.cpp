/**
 * https://docs.openvino.ai/2025/get-started/install-openvino/install-openvino-archive-windows.html
 * 
 * ovc model.onnx
 */

#include "spdlog/spdlog.h"

#include "openvino/openvino.hpp"

static long long total = 0;

int main() {
    std::cout << ov::get_openvino_version().buildNumber << "\n";
    std::cout << ov::get_openvino_version().description << "\n";
    ov::Core core;
    std::shared_ptr<ov::Model> model = core.read_model("./yolo11n.xml");
    ov::element::Type input_type = ov::element::u8;
    std::vector<unsigned char> data(3 * 640 * 640);
    ov::Shape input_shape = { 1, 640, 640, 3 };
    ov::Tensor input_tensor = ov::Tensor(input_type, input_shape, data.data());
    const ov::Layout tensor_layout{"NHWC"};
    ov::preprocess::PrePostProcessor ppp(model);
    ppp.input().tensor().set_shape(input_shape).set_element_type(input_type).set_layout(tensor_layout);
    ppp.input().preprocess().resize(ov::preprocess::ResizeAlgorithm::RESIZE_LINEAR);
    ppp.input().model().set_layout("NCHW");
    ppp.output().tensor().set_element_type(ov::element::f32);
    model = ppp.build();
    ov::CompiledModel compiled_model = core.compile_model(model, "CPU");
    for(int i = 0; i < 100; ++i) {
        auto a = std::chrono::system_clock::now();
        ov::InferRequest infer_request = compiled_model.create_infer_request();
        infer_request.set_input_tensor(input_tensor);
        infer_request.infer();
        // infer_request.start_async();
        // infer_request.wait();
        const ov::Tensor& output_tensor = infer_request.get_output_tensor();
        // std::cout << output_tensor.get_shape() << '\n';
        auto z = std::chrono::system_clock::now();
        auto count = std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count();
        total += count;
        SPDLOG_INFO("时间消耗：{}", std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count());
    }
    SPDLOG_INFO("平均时间消耗：{}", total / 100);
    std::cout << "完成\n";
    return 0;
}
