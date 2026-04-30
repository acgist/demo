#include "opencv2/opencv.hpp"

int main() {
    auto apple    = cv::imread("D:/tmp/apple.jpg");
    auto apple_pi = cv::imread("D:/tmp/apple_pi.png");
    cv::Mat hist;
    const int channels[1] = { 0   }; // 通道索引
    const int histSize[1] = { 256 }; // 直方图的维度
    const float  ranges_a[2] = { 0, 255   };
    const float* ranges_b[1] = { ranges_a }; // 像素灰度范围
    cv::calcHist(&apple_pi, 1, channels, cv::Mat{}, hist, 1, histSize, ranges_b);
    cv::Mat ret;
    cv::calcBackProject(&apple, 1, channels, hist, ret, ranges_b);
    cv::imshow("app", apple);
    cv::imshow("ret", ret);
    cv::waitKey();
    cv::Mat m_ret;
    // cv::matchTemplate(apple, apple_pi, m_ret, cv::TM_SQDIFF);
    cv::matchTemplate(apple, apple_pi, m_ret, cv::TM_CCOEFF_NORMED);
    double min, max;
    cv::Point min_point, max_point;
    cv::minMaxLoc(m_ret, &min, &max, &min_point, &max_point);
    cv::rectangle(apple, cv::Rect(min_point.x, min_point.y, apple_pi.cols, apple_pi.rows), cv::Scalar(  0, 0,   0), 2);
    cv::rectangle(apple, cv::Rect(max_point.x, max_point.y, apple_pi.cols, apple_pi.rows), cv::Scalar(255, 0, 255), 2);
    cv::imshow("mret", m_ret);
    cv::imshow("a_pi", apple_pi);
    cv::imshow("rect", apple);
    cv::waitKey();
    return 0;
}
