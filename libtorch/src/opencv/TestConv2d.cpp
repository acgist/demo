#include "opencv2/opencv.hpp"

int main() {
    auto image = cv::imread("D:/tmp/ycx.jpg");
    cv::imshow("image", image);
    cv::waitKey();
    cv::Mat kernel = (cv::Mat_<float>(3, 3) << 1, 0, 0, 0, 1, 0, 0, 0, 1);
    // cv::Mat kernel = (cv::Mat_<float>(3, 3) << 1, 2, 1, 2, 0, 2, 1, 2, 1);
    cv::Mat result;
    // cv::filter2D(image, result, -1, kernel);
    cv::filter2D(image, result, CV_8U, kernel);
    // cv::filter2D(image, result, CV_32F, kernel);
    cv::imshow("result", result);
    cv::waitKey();
    return 0;
}
