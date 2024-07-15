#include "../header/LibTorch.hpp"

#include "torch/torch.h"
#include "torch/script.h"

#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"

static void testInit();
static void testClone();
static void testResize();
static void testSlice();
static void testOperator();
static void testSize();
static void testArgmax();
static void testSqueeze();
static void testPermute();
static void testImage();
static void testFind();
static void testTransfor();
static void testEqual();
static void testCUDA();
static void testGrad();

void lifuren::testTensor() {
    std::cout << "是否支持CUDA：" << torch::cuda::is_available() << '\n';
    std::cout << "是否支持CUDNN：" << torch::cuda::cudnn_is_available() << '\n';
    testInit();
    testClone();
    testResize();
    testSize();
    testArgmax();
    testSqueeze();
    testPermute();
    testImage();
    testFind();
    testTransfor();
    testEqual();
    testCUDA();
    testGrad();
}

static void testInit() {
    auto a = torch::zeros({3, 4});
    std::cout << "a =\r\n" << a << '\n';
    a = torch::ones({3, 4});
    std::cout << "a =\r\n" << a << '\n';
    a = torch::eye(4);
    std::cout << "a =\r\n" << a << '\n';
    a = torch::full({3, 4}, 10);
    std::cout << "a =\r\n" << a << '\n';
    a = torch::tensor({33, 22, 11});
    std::cout << "a =\r\n" << a << '\n';
    // 随机
    a = torch::rand({ 3, 4 });
    std::cout << "a =\r\n" << a << '\n';
    // 正态分布随机
    a = torch::randn({ 3, 4 });
    std::cout << "a =\r\n" << a << '\n';
    a = torch::randint(0, 4, { 3, 4 });
    std::cout << "a =\r\n" << a << '\n';
    int array[10] = { 3, 4, 5, 1, 2, 3 };
    a = torch::from_blob(array, { 3, 2 }, torch::kFloat);
    std::cout << "a =\r\n" << a << '\n';
    std::vector<float> vector{ 3, 4, 5, 1, 2, 3 };
    a = torch::from_blob(vector.data(), { 3, 2 }, torch::kFloat);
    std::cout << "a =\r\n" << a << '\n';
}

static void testClone() {
    auto a = torch::zeros({3, 4});
    // 浅拷贝
    auto b = a;
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "b =\r\n" << b << '\n';
    a[0][0] = 1;
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "b =\r\n" << b << '\n';
    // 深拷贝
    auto c = a.clone();
    std::cout << "c =\r\n" << a << '\n';
    std::cout << "c =\r\n" << c << '\n';
    a[0][0] = 2;
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "c =\r\n" << c << '\n';
    // 已有尺寸建立新的张量
    // auto d = torch::zeros_like(b);
    // auto d = torch::ones_like(b);
    // auto d = torch::rand_like(b, torch::kFloat);
}

static void testResize() {
    // TODO: flatten、view、reshape、transpose
    int array[6] = { 4, 5, 6, 1, 2, 3 };
    auto a = torch::from_blob(array, { 3, 2 }, torch::kInt);
    std::cout << "a =\r\n" << a << '\n';
    auto b = a.view({ 2, 3 });
    // auto b = a.view({ 1, 2, -1 });
    std::cout << "b =\r\n" << b << '\n';
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "a =\r\n" << a.sizes() << '\n';
    std::cout << "a =\r\n" << a[0] << '\n';
    std::cout << "a =\r\n" << a[0][0] << '\n';
}

static void testSlice() {
    // TODO: narrow、select、index、index_put_、index_select、slice
}

static void testOperator() {
    int arrayA[] = { 1, 2, 3, 4 };
    int arrayB[] = { 1, 2, 3, 4 };
    const torch::Tensor lineA = torch::tensor({ 1, 2, 3, 4});
    const torch::Tensor lineB = torch::tensor({ 1, 2, 3, 4});
    const torch::Tensor a = torch::from_blob(arrayA, { 2, 2 }, torch::kInt);
    const torch::Tensor b = torch::from_blob(arrayB, { 2, 2 }, torch::kInt);
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "b =\r\n" << b << '\n';
    std::cout << "a + b  =\r\n" << (a + b) << '\n';
    std::cout << "a - b  =\r\n" << (a - b) << '\n';
    std::cout << "a * b  =\r\n" << (a * b) << '\n';
    std::cout << "a / b  =\r\n" << (a / b) << '\n';
    std::cout << "a % b  =\r\n" << (a % b) << '\n';
    std::cout << "a == b =\r\n" << (a == b) << '\n';
    std::cout << "lineA dot lineB   =\r\n" << lineA.dot(lineB) << '\n';
    std::cout << "lineA dot lineB.t =\r\n" << lineA.dot(lineB.t()) << '\n';
    // TODO: cat、stack
}

static void testSize() {
    int array[6] = { 4, 5, 6, 1, 2, 3 };
    auto a = torch::from_blob(array, { 3, 2 }, torch::kInt);
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "a =\r\n" << a.size(0) << '\n';
    std::cout << "a =\r\n" << a.sizes() << '\n';
}

static void testArgmax() {
    int array[6] = { 4, 5, 6, 1, 2, 3 };
    auto a = torch::from_blob(array, { 3, 2 }, torch::kInt);
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "a =\r\n" << a.argmax(1) << '\n';
    std::cout << "a =\r\n" << a.argmax(1, true) << '\n';
}

static void testSqueeze() {
    int array[6] = { 4, 5, 6, 1, 2, 3 };
    auto a = torch::from_blob(array, { 3, 2 }, torch::kInt);
    std::cout << "a =\r\n" << a << '\n';
    // a = a.unsqueeze(0);
    a = a.unsqueeze(1);
    a = a.unsqueeze(2);
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "a =\r\n" << a.squeeze() << '\n';
}

static void testPermute() {
    torch::Tensor a = torch::linspace(1, 30, 30).view({ 3, 2, 5 });
    std::cout << "a =\r\n" << a.sizes() << '\n';
    std::cout << "a =\r\n" << a.permute({ 0, 1, 2 }).sizes() << '\n';
    std::cout << "a =\r\n" << a.permute({ 0, 2, 1 }).sizes() << '\n';
    std::cout << "a =\r\n" << a.permute({ 1, 0, 2 }).sizes() << '\n';
    std::cout << "a =\r\n" << a.permute({ 1, 2, 0 }).sizes() << '\n';
    std::cout << "a =\r\n" << a.permute({ 2, 0, 1 }).sizes() << '\n';
    std::cout << "a =\r\n" << a.permute({ 2, 1, 0 }).sizes() << '\n';
    std::cout << "a =\r\n" << a << '\n';
    std::cout << "a =\r\n" << a.permute({ 0, 1, 2 }) << '\n';
    std::cout << "a =\r\n" << a.permute({ 0, 2, 1 }) << '\n';
    std::cout << "a =\r\n" << a.permute({ 1, 0, 2 }) << '\n';
    std::cout << "a =\r\n" << a.permute({ 1, 2, 0 }) << '\n';
    std::cout << "a =\r\n" << a.permute({ 2, 0, 1 }) << '\n';
    std::cout << "a =\r\n" << a.permute({ 2, 1, 0 }) << '\n';
}

static void testImage() {
    cv::Mat image = cv::imread("D://tmp/logo.png");
    cv::resize(image, image, cv::Size(200, 200));
    torch::Tensor a = torch::from_blob(image.data, { image.rows, image.cols, 3 }, torch::kByte);
    std::cout << "a = \r\n" << a.sizes() << '\n';
    std::cout << "a = \r\n" << a.permute({2, 0, 1}).sizes() << '\n';
    std::cout << "a = \r\n" << a.sizes() << '\n';
    std::cout << "a = \r\n" << a.dim() << '\n';
    // torch::Tensor b = torch::max_pool2d(a, 2);
    // cv::imwrite("D://tmp/logo.max.png", b);
}

static void testFind() {
    int array[12] = {
        4,  5,  6,
        14, 35, 16,
        24, 25, 26,
        1,  2,  3
    };
    auto a = torch::from_blob(array, { 4, 3 }, torch::kInt);
    std::cout << "a = \r\n" << a << '\n';
    std::cout << "a sizes  = \r\n" << a.sizes() << '\n';
    std::cout << "a slice  = \r\n" << a.slice(0, 0, 2) << '\n';
    std::cout << "a slice  = \r\n" << a.slice(1, 0, 2) << '\n';
    auto [ key, value ] = a.max(1);
    std::cout << "a max    = \r\n" << key << '\n';
    std::cout << "a max    = \r\n" << value << '\n';
    std::cout << "a argmax = \r\n" << a.argmax(1) << '\n';
    std::cout << "a unsqueeze = \r\n" << a.unsqueeze(0) << '\n';
    std::cout << "a unsqueeze = \r\n" << a.unsqueeze(1) << '\n';
}

static void testTransfor() {
    int array[12] = {
        4,  5,  6,
        14, 35, 16,
        24, 25, 26,
        1,  2,  3
    };
    auto a = torch::from_blob(array, { 4, 3 }, torch::kInt);
    std::cout << "a = \r\n" << a << '\n';
    std::cout << "a = \r\n" << a[0, 1] << '\n';
    // std::cout << "a = \r\n{}", a[0, 2]);
    std::cout << "a = \r\n" << a[1, 1] << '\n';
    // std::cout << "a = \r\n" << a[1, 2]) << '\n';
    std::cout << "a = \r\n" << a[2, 1] << '\n';
    // std::cout << "a = \r\n" << a[2, 2]) << '\n';
    std::cout << "a = \r\n" << a.slice(0, 1) << '\n';
    std::cout << "a = \r\n" << a.slice(1, 1) << '\n';
    std::cout << "a = \r\n" << a.view(12) << '\n';
    std::cout << "a = \r\n" << a.view({4, 3, 1, 1}) << '\n';
    std::cout << "a = \r\n" << a[0][1].template item<int>() << '\n';
}

static void testEqual() {
    torch::Tensor a = torch::rand({ 2, 3 });
    torch::Tensor b = a.view({ 2, 3 });
    std::cout << "a = " << a << '\n';
    std::cout << "a = " << b << '\n';
    std::cout << "a = " << (long long) &a << '\n';
    std::cout << "a = " << (long long) &b << '\n';
    std::cout << "a = b: " << (a == b) << '\n';
    int array[6] = { 4, 5, 6, 1, 2, 3 };
    auto ax = torch::from_blob(array, { 3, 2 }, torch::kInt);
    ax += 100;
    std::cout << "ax = " << ax << '\n';
    for(auto& v : array) {
        std::cout << "axv = " << v << '\n';
    }
}

static void testCUDA() {
    if(!torch::cuda::is_available()) {
        return;
    }
    auto device = torch::kCUDA;
    torch::tensor({33, 22, 11}).to(device);
}

static void testGrad() {
    torch::Tensor a = torch::ones({2, 3});
    // a.detach();
    // a.requires_grad = true;
    // a.set_requires_grad(false);
    a.set_requires_grad(true);
    try {
        // a.backward();
        auto b = torch::rand({2, 3});
        std::cout << "a = " << a << '\n';
        std::cout << "b = " << b << '\n';
        a.backward(b);
        // a.backward(torch::zeros({2, 3}));
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
    // {
    //     torch::NoGradGuard noGradGuard;
    //     // 不算梯度
    // }
    std::cout << "a grad = " << a.grad() << '\n';
    // a.is_leaf();
    // std::cout << "a grad = {}", a.grad_fn());
    try {
        auto xa = torch::tensor({1, 2, 3, 4}, torch::kFloat64);
        xa.set_requires_grad(true);
        auto xb = 2 * xa;
        auto xc = xb.view({2, 2});
        std::cout << "xc = " << xc << '\n';
        auto xd = torch::tensor({{1.0, 0.1}, {0.01, 0.001}});
        std::cout << "xd = " << xd << '\n';
        xc.backward(xd);
        std::cout << "xa grad = " << xa.grad() << '\n';
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
}

int main(const int argc, const char * const argv[]) {
    lifuren::testTensor();
    return 0;
}
