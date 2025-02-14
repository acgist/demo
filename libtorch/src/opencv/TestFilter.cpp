#include "opencv2/opencv.hpp"

#include <iostream>

int main() {
    // /**
    //  * 低通滤波去掉噪声
    //  */
    auto image = cv::imread("D:/tmp/lena.png");
    cv::imshow("image", image);
    cv::Mat target_3;
    cv::Mat target_9;
    // 低通滤波：
    // 高通滤波：
    // 线性滤波：均值、方框、高斯
    // 均值滤波
    cv::blur(image, target_3, cv::Size(3, 3));
    cv::blur(image, target_9, cv::Size(9, 9));
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    // 方框滤波
    cv::boxFilter(image, target_3, -1, cv::Size(3, 3));
    cv::boxFilter(image, target_9, -1, cv::Size(9, 9));
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    cv::boxFilter(image, target_3, -1, cv::Size(3, 3), cv::Point(-1, -1), false);
    cv::boxFilter(image, target_9, -1, cv::Size(9, 9), cv::Point(-1, -1), false);
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    // 高斯滤波
    cv::GaussianBlur(image, target_3, cv::Size(3, 3), 10);
    cv::GaussianBlur(image, target_9, cv::Size(9, 9), 10);
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    cv::GaussianBlur(image, target_3, cv::Size(3, 3), 0, 10);
    cv::GaussianBlur(image, target_9, cv::Size(9, 9), 0, 10);
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    cv::GaussianBlur(image, target_3, cv::Size(3, 3), 10, 10);
    cv::GaussianBlur(image, target_9, cv::Size(9, 9), 10, 10);
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    // 可分离滤波
    // 非线性滤波：中值、双边
    // 中值滤波
    cv::medianBlur(image, target_3, 3);
    cv::medianBlur(image, target_9, 9);
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    // 双边滤波
    cv::bilateralFilter(image, target_3,  9, 50, 20.0);
    cv::bilateralFilter(image, target_3, 25, 50, 20.0);
    cv::imshow("target_3", target_3);
    cv::imshow("target_9", target_9);
    cv::waitKey();
    cv::Mat dst;
    cv::Mat src = (cv::Mat_<float>(4, 4) <<
        0, 2, 1, 3,
        2, 4, 0, 0,
        2, 3, 2, 0,
        2, 1, 0, 3
    );
    cv::Mat mask = (cv::Mat_<float>(3, 3) <<
        1, 2, 3,
        4, 5, 6,
        7, 8, 9
    );
    // cv::filter2D(src, dst, -1, mask, cv::Point(-1, -1), 0, cv::BORDER_REFLECT);
    // cv::filter2D(src, dst, -1, mask, cv::Point(-1, -1), 0, cv::BORDER_DEFAULT);
    cv::filter2D(src, dst, -1, mask, cv::Point(-1, -1), 0, cv::BORDER_CONSTANT);
    // cv::filter2D(src, dst, -1, mask, cv::Point(-1, -1), 0, cv::BORDER_REPLICATE);
    std::cout << dst << '\n';
    return 0;
}
