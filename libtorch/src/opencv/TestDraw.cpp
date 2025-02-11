#include "opencv2/opencv.hpp"

int main() {
    cv::Mat mat(200, 200, CV_8UC3);
    cv::imshow("image", mat);
    cv::waitKey();
    cv::circle(mat, cv::Point(100, 100), 50, cv::Scalar(0, 0, 0));
    cv::imshow("image", mat);
    cv::waitKey();
    cv::ellipse(mat, cv::Point(100, 100), cv::Size(50, 100), 0, 0, 180, cv::Scalar(0, 0, 0));
    // cv::ellipse(mat, cv::Point(100, 100), cv::Size(50, 100), 0, 0, 360, cv::Scalar(0, 0, 0));
    cv::imshow("image", mat);
    cv::waitKey();
    mat = cv::imread("D:/tmp/xxx.jpg");
    cv::imshow("image", mat);
    cv::waitKey();
    // 高斯金字塔
    cv::pyrDown(mat, mat);
    cv::imshow("image", mat);
    cv::waitKey();
    // 拉普拉斯金字塔
    cv::pyrUp(mat, mat);
    cv::imshow("image", mat);
    cv::waitKey();
    return 0;
}
