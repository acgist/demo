#include "opencv2/opencv.hpp"

int main() {
    auto image { cv::imread("D://tmp/xxx.jpg") };
    cv::Point2f src[4];
    cv::Point2f dst[4];
    src[0] = cv::Point2f(0,                image.rows);
    src[1] = cv::Point2f(image.cols,       image.rows);
    src[2] = cv::Point2f(0,                0);
    src[3] = cv::Point2f(image.cols,       0);
    dst[0] = cv::Point2f(0.2 * image.cols, 0.8 * image.rows);
    dst[1] = cv::Point2f(0.8 * image.cols, 0.8 * image.rows);
    dst[2] = cv::Point2f(0,                0);
    dst[3] = cv::Point2f(image.cols,       0);
    cv::imshow("image", image);
    cv::waitKey();
    cv::warpPerspective(image, image, cv::getPerspectiveTransform(src, dst), image.size());
    cv::imshow("image", image);
    cv::waitKey();
    return 0;
}