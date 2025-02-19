#include "opencv2/opencv.hpp"

int main() {
    auto image_1 = cv::imread("D:/tmp/lena.png");
    auto image_2 = cv::imread("D:/tmp/lena-line.png");
    cv::Mat dst;
    // 差值法检测移动物体
    cv::absdiff(image_1, image_2, dst);
    cv::imshow("dst", dst);
    cv::waitKey();
    return 0;
}