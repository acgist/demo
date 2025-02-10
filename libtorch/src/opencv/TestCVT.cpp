#include "opencv2/opencv.hpp"

int main() {
    auto image { cv::imread("D:/tmp/xxx.jpg") };
    cv::imshow("image", image);
    cv::waitKey();
    cv::cvtColor(image, image, cv::COLOR_BGR2RGB);
    cv::imshow("image", image);
    cv::waitKey();
    // image.convertTo()
    return 0;
}