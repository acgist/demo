#include <iostream>

#include "opencv2/opencv.hpp"

int main() {
    // auto image { cv::imread("D:/tmp/xxx.jpg") };
    auto image { cv::imread("D:/tmp/yang.png") };
    double min, max;
    cv::Point minIndex, maxIndex;
    // image = image.reshape(1, 0);
    auto flat = image.reshape(1, image.rows * 3);
    cv::minMaxLoc(flat, &min, &max, &minIndex, &maxIndex);
    std::cout << min << " = " << max << " = " << minIndex << " = " << maxIndex << '\n';
    auto mean = cv::mean(image);
    std::cout << mean << '\n';
    cv::Mat avg, std;
    cv::meanStdDev(image, avg, std);
    std::cout << avg << '\n';
    std::cout << std << '\n';
    auto image_1 = cv::imread("D://tmp/1.jpg");
    auto image_3 = cv::imread("D://tmp/3.jpg");
    cv::Mat mix;
    // cv::max(image_1, image_3, mix);
    // cv::min(image_1, image_3, mix);
    cv::bitwise_or(image_1, image_3, mix);
    // cv::bitwise_not(image_1, image_3, mix);
    // cv::bitwise_xor(image_1, image_3, mix);
    // cv::bitwise_and(image_1, image_3, mix);
    cv::imshow("mix", mix);
    cv::waitKey();
    // cv::threshold(mix, mix, 127, 255, cv::THRESH_BINARY);
    cv::threshold(mix, mix, 127, 255, cv::THRESH_BINARY_INV);
    cv::imshow("mix", mix);
    cv::waitKey();
    uchar lut_array[256];
    for(int i = 0; i < 256; ++i) {
        lut_array[i] = i / 100 * 100;
    }
    cv::Mat lut { 1, 256, CV_8UC1, lut_array };
    cv::LUT(image_1, lut, mix);
    cv::imshow("mix", image_1);
    cv::waitKey();
    cv::imshow("mix", mix);
    cv::waitKey();
    cv::hconcat(image_1, image_3, mix);
    cv::imshow("mix", mix);
    cv::waitKey();
    return 0;
}