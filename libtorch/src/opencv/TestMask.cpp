#include <iostream>

#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/objdetect.hpp"

int main() {
    // cv::Mat mat { 400, 600, CV_8UC1 };
    // auto* data00 = mat.ptr<uchar>(0);
    cv::Mat img { cv::imread("D:/tmp/bike.jpg") };
    cv::imshow("image", img);
    cv::waitKey();
    double a = cv::getTickCount();
    cv::Mat kernel = (cv::Mat_<char>(1, 3) << 1, 0, 1);
    // cv::Mat kernel = (cv::Mat_<char>(3, 1) << 1, 0, 1);
    // cv::Mat kernel = (cv::Mat_<char>(3, 3) << 0, -1, 0, -1, 5, -1, 0, -1, 0);
    double z = cv::getTickCount();
    std::cout << ((z - a) / cv::getTickFrequency()) << '\n';
    // cv::Mat dst;
    cv::filter2D(img, img, img.depth(), kernel);
    cv::imshow("image", img);
    cv::waitKey();
    return 0;
}
