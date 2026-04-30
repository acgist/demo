#include <iostream>

#include "torch/torch.h"
#include "torch/script.h"

#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"

int main() {
    // long long ai[] { 1, 2, 3 };
    // auto a = torch::from_blob(ai, {3, 1}, torch::kLong);
    // std::cout << a << "\n";
    // long long bi[] { 1, 2, 3 };
    // auto b = torch::tensor(bi, torch::kLong);
    // std::cout << b << "\n";
    // auto c = torch::tensor({ 1, 2, 3 }, torch::kLong);
    // std::cout << c << "\n";
    // auto d = torch::tensor({ 1, 2, 3 }, torch::kLong).unsqueeze(1);
    // std::cout << d << "\n";
    // std::cout << d.sizes() << "\n";
    // std::cout << d.sizes()[0] << "\n";

    // auto a = torch::ones({1, 2});
    // auto b = torch::ones({2, 3});
    // std::cout << (a * a) << "\n";
    // std::cout << (a.mul(a)) << "\n";
    // std::cout << (torch::mm(a, b)) << "\n";
    // std::cout << (torch::matmul(a, b)) << "\n";

    // std::vector<float> v = {
    //     2.1, 3.3, 3.6, 4.4, 5.5, 6.3, 6.5, 7.0, 7.5, 9.7, // x
    //     1.0, 1.2, 1.9, 2.0, 2.5, 2.5, 2.2, 2.7, 3.0, 3.6  // y
    // }; 
    // torch::Tensor data_tensor = torch::from_blob(v.data(), {2, 10}, torch::kFloat32);
    // std::cout << data_tensor << "\n";
    // std::cout << data_tensor.transpose(0, 1) << "\n";

    // auto a = torch::ones({2, 3});
    // auto b = torch::ones({2, 3});
    // std::cout << torch::cat({a, b}, 0) << "\n";
    // std::cout << torch::cat({a, b}, 1) << "\n";
    // std::cout << torch::cat({a, b}, 0).sizes() << "\n";
    // std::cout << torch::cat({a, b}, 1).sizes() << "\n";

    // auto a = torch::ones({2, 3});
    // auto b = torch::ones({2, 1});
    // std::cout << torch::cat({a, b}, 1) << "\n";
    // std::cout << torch::cat({a, b}, 1).sizes() << "\n";

    // auto a = torch::ones({2, 3});
    // auto b = torch::ones({2}).unsqueeze(1);
    // std::cout << b.sizes() << "\n";
    // std::cout << torch::cat({a, b}, 1) << "\n";
    // std::cout << torch::cat({a, b}, 1).sizes() << "\n";
    
    // auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    // std::cout << t << "\n";
    // std::cout << t.sum() << "\n";
    // std::cout << t.sum(0, true) << "\n";
    // std::cout << t.sum(1, true) << "\n";
    // std::cout << t.sum(0, false) << "\n";
    // std::cout << t.sum(1, false) << "\n";

    // auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    // // auto e = t.clone();
    // auto e = t.exp();
    // auto s = e.sum(1, true);
    // std::cout << t << "\n";
    // std::cout << e << "\n";
    // std::cout << s << "\n";
    // auto softmax = t / s;
    // std::cout << softmax << "\n";

    // auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    // // std::cout << t.view({-1, 4}) << "\n";
    // std::cout << t.view({-1, 3}) << "\n";
    // std::cout << t.view({-1, 2}) << "\n";
    // std::cout << t.view({-1}) << "\n";
    // std::cout << t.flatten() << "\n";
    // std::cout << t.flatten(1) << "\n";

    // auto t = torch::tensor({{1, 2, 3}, {4, 5, 6}});
    // std::cout << t.argmax()  << "\n";
    // std::cout << t.argmax(1) << "\n";
    // // t.grad().data().zero_();

    // auto a = torch::arange(25).reshape({5, 5});
    // auto b = torch::einsum("ii", a);
    // std::cout << a << "\n";
    // std::cout << b << "\n";
    // std::cout << torch::max(a) << '\n';
    // std::cout << torch::argmax(a) << '\n';
    // std::cout << torch::argmax(a, 1) << '\n';

    // auto a = torch::arange(100).reshape({5, 4, 5});
    // std::cout << a << "\n";
    // std::cout << torch::max(a) << '\n';
    // std::cout << torch::argmax(a) << '\n';
    // std::cout << torch::argmax(a, 0) << '\n';
    // std::cout << torch::argmax(a, 1) << '\n';
    // std::cout << torch::argmax(a, 2) << '\n';
    // std::cout << torch::nonzero(a) << '\n';

    // auto a = torch::randn({10, 2});
    // std::cout << a << "\n";
    // std::cout << a * 10 << "\n";
    // std::cout << torch::rand({4, 3, 2}) << "\n";
    // std::cout << torch::randint(1, 10, {2, 2}, torch::kFloat16) << "\n";

    // auto a = torch::randn({10, 2});
    // auto b = torch::randn({10, 2});
    // std::cout << a << '\n';
    // std::cout << b << '\n';
    // std::cout << torch::cat({a, b}, 0) << '\n';
    // std::cout << torch::cat({a, b}, 1) << '\n';
    // auto c = torch::empty({10, 1}).fill_(1);
    // std::cout << c << '\n';
    // std::cout << torch::randn({10, 2}) + 100 << '\n';
    // std::cout << torch::randint(0, 10, {10, 10}, torch::kFloat) << '\n';
    // std::cout << torch::randint(0, 10, {10, 10}, torch::kFloat) / 10 << '\n';
    // std::cout << a.numel() << "\n";
    // std::cout << a.sizes() << "\n";

    // std::cout << "1=" << torch::tensor({1.0F, 2.0F, 3.0F}, torch::kFloat32) << '\n';
    // std::cout << "2=" << torch::tensor({1.0F, 2.0F, 3.0F}, torch::kFloat32).sizes() << '\n';
    // std::cout << "3=" << torch::tensor({1.0F, 2.0F, 3.0F}, torch::kFloat32).unsqueeze(0) << '\n';
    // std::cout << "4=" << torch::tensor({1.0F, 2.0F, 3.0F}, torch::kFloat32).unsqueeze(1) << '\n';

    // auto a = torch::randn({3, 4});
    // std::cout << a << '\n';
    // auto mask = a < 0.5F;
    // std::cout << mask << '\n';
    // std::cout << (mask * a) << '\n';

    // auto image = cv::imread("D:/tmp/bike.jpg");
    // torch::Tensor a = torch::from_blob(image.data, { image.rows, image.cols, 3 }, torch::kByte);
    // // torch::Tensor a = torch::from_blob(image.data, { image.rows, image.cols, 3 }, torch::kByte).permute({ 2, 0, 1 });
    // std::cout << a.sizes() << '\n';

    // torch::TensorOptions options;
    // options = options.requires_grad(true);
    // auto a = torch::randn({4}, options);
    // auto b = a * 2;
    // auto d = b.mean();
    // // auto c = b * b * 4;
    // // auto d = c.mean();
    // std::cout << a << '\n';
    // std::cout << b << '\n';
    // // std::cout << c << '\n';
    // std::cout << d << '\n';
    // d.backward();
    // std::cout << "a.grad = " << a.grad() << '\n';
    // // std::cout << "b.grad = " << b.grad() << '\n';
    // // std::cout << "c.grad = " << c.grad() << '\n';
    // // std::cout << "d.grad = " << d.grad() << '\n';

    // torch::Tensor input = torch::tensor({
    //     {1, 2, 3, 4},
    //     {2, 2, 4, 4},
    //     {6, 2, 1, 4}
    // }, torch::kFloat);
    // // torch::Tensor input = torch::ones({2, 10});
    // // torch::Tensor input = torch::randn({2, 10});
    // std::cout << input << '\n';
    // // torch::nn::functional::normalize();
    // // float epsilon = 1e-5;
    // // torch::nn::BatchNormFuncOptions().momentum(0.1).eps(epsilon);
    // // torch::nn::functional::batch_norm();
    // // torch::nn::functional::normalize(input, torch::nn::BatchNormOptions{}.momentum(0.1).eps(1e-8));
    // // auto output = torch::nn::functional::normalize(input, torch::nn::functional::NormalizeFuncOptions{});
    // auto output = torch::nn::functional::normalize(input, torch::nn::functional::NormalizeFuncOptions{}.dim(0).eps(1e-8));
    // // std::cout << input.narrow_copy(0, 0, 1) << '\n';
    // // std::cout << input.narrow_copy(0, 0, 2) << '\n';
    // // std::cout << input.narrow_copy(0, 1, 1) << '\n';
    // // std::cout << input.narrow_copy(0, 1, 2) << '\n';
    // // std::cout << input.narrow_copy(1, 0, 1) << '\n';
    // // std::cout << input.narrow_copy(1, 0, 2) << '\n';
    // // std::cout << input.narrow_copy(1, 1, 1) << '\n';
    // // std::cout << input.narrow_copy(1, 1, 2) << '\n';
    // std::cout << input[0] << '\n';
    // std::cout << output << '\n';
    // std::cout << output.sizes() << '\n';

    // torch::Tensor tensor = torch::randn({4, 3});
    // std::cout << tensor << '\n';
    // std::cout << tensor.norm() << '\n';
    // torch::nn::init::normal_(tensor, 0, 0.1);
    // std::cout << tensor << '\n';
    // std::cout << tensor.norm() << '\n';
    // auto b = torch::randn({3, 4});
    // std::cout << b << '\n';
    // torch::nn::init::constant_(b, 0);
    // std::cout << b << '\n';
    // torch::nn::init::uniform_(b, -10.0F, 10.0F);
    // std::cout << b << '\n';
    // torch::nn::init::normal_(b, 1.0F, 5.0F);
    // std::cout << b << '\n';

    // auto a = torch::randn({3, 4});
    // std::cout << a << '\n';
    // std::cout << (a - a.mean()) << '\n';
    // std::cout << std::fixed << std::setprecision(6) << (a - a.mean()).sum().item<float>() << '\n';
    // std::cout << std::fixed << std::setprecision(6) << (a - a.mean()).mean().item<float>() << '\n';
    // // const float ret = (a - a.mean()).mean().item<float>();
    // // std::cout << std::fixed << std::setprecision(6) << ret << '\n';

    // torch::nn::ParameterDict
    // torch::nn::ParameterList
    // torch::nn::ParameterList list;
    // list->append()
    // torch::nn::ParameterDict dict;
    // dict->insert()

    // auto a = torch::randn({3, 4});
    // // auto a = torch::tensor({3, 4});
    // torch::save(a, "D:/tmp/tensor.pt");
    // torch::Tensor b;
    // torch::load(b, "D:/tmp/tensor.pt");
    // std::cout << a << '\n';
    // std::cout << b << '\n';
    // std::cout << (a == b) << "\n";
    // // torch::arange(25).reshape({5, 5});

    auto a = torch::randn({2, 2});
    // std::cout << "* = " << (a * a);
    // std::cout <<  "mul = " << (a.mul(a));
    // std::cout <<  "mm = " << (a.mm(a));
    // std::cout <<  "matmul = " << (a.matmul(a));
    // std::cout <<  "dot = " << (a.dot(a));
    std::cout << a << '\n';
    std::cout << a.numel() << '\n';
    std::cout << a.element_size() << '\n';
    return 0;
}
