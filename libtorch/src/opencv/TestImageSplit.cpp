#include "opencv2/opencv.hpp"

int main() {
    // 离散傅里叶变换
    // 图片分割
    // 漫水填充法
    auto image = cv::imread("D:/tmp/lena.png");
    cv::imshow("image", image);
    cv::RNG rng;
    int connectivity = 4; // 连通邻域方式
    int maskVal = 255;
    int flags = connectivity | (maskVal << 8) | cv::FLOODFILL_FIXED_RANGE; // 漫水填充操作方式标志
    cv::Scalar loDiff(20, 20, 20);
    cv::Scalar upDiff(20, 20, 20);
    cv::Mat mask = cv::Mat::zeros(image.rows + 2, image.cols + 2, CV_8UC1);
    while(true) {
        int py = rng.uniform(0, image.rows - 1);
        int px = rng.uniform(0, image.cols - 1);
        cv::Point point(px, py);
        cv::Scalar newVal(rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255));
        int area = cv::floodFill(image, mask, point, newVal, nullptr, loDiff, upDiff, flags);
        cv::imshow("image", image);
        cv::imshow("mask", mask);
        int c = cv::waitKey();
        if((c & 0xFF) == 'e') {
            break;
        }
    }
    // 分水岭法
    // grabcut法
    // mean-shift法
    // kmeans法
    return 0;
}