#include "opencv2/opencv.hpp"

int main() {
    // 边缘检测
    // auto image = cv::imread("D:/tmp/lena.png");
    // auto image = cv::imread("D:/tmp/lena.png", cv::IMREAD_ANYCOLOR);
    auto image = cv::imread("D:/tmp/lena.png", cv::IMREAD_GRAYSCALE);
    // auto image = cv::imread("D:/tmp/ycx.jpg");
    // auto image = cv::imread("D:/tmp/ycx.jpg", cv::IMREAD_GRAYSCALE);
    cv::Mat k_1  = (cv::Mat_<float>(1, 2) << 1, -1);
    cv::Mat k_2  = (cv::Mat_<float>(1, 3) << 1, 0, -1);
    cv::Mat k_3  = (cv::Mat_<float>(3, 1) << 1, 0, -1);
    cv::Mat k_xy = (cv::Mat_<float>(2, 2) << 1, 0, 0, -1);
    cv::Mat k_yx = (cv::Mat_<float>(2, 2) << 0, -1, 1, 0);
    cv::Mat i_1, i_2, i_3, i_xy, i_yx;
    cv::filter2D(image, i_1,  CV_16S, k_1);
    cv::filter2D(image, i_2,  CV_16S, k_2);
    cv::filter2D(image, i_3,  CV_16S, k_3);
    cv::filter2D(image, i_xy, CV_16S, k_xy);
    cv::filter2D(image, i_yx, CV_16S, k_yx);
    cv::convertScaleAbs(i_1,  i_1);
    cv::convertScaleAbs(i_2,  i_2);
    cv::convertScaleAbs(i_3,  i_3);
    cv::convertScaleAbs(i_xy, i_xy);
    cv::convertScaleAbs(i_yx, i_yx);
    cv::imshow("image", image);
    cv::imshow("i_1",  i_1);
    cv::imshow("i_2",  i_2);
    cv::imshow("i_3",  i_3);
    cv::imshow("i_xy", i_xy);
    cv::imshow("i_yx", i_yx);
    cv::waitKey();
    cv::destroyAllWindows();
    cv::Sobel(image, k_1, CV_16S, 2, 0, 3);
    cv::Sobel(image, k_2, CV_16S, 0, 2, 3);
    cv::convertScaleAbs(i_1,  i_1);
    cv::convertScaleAbs(i_2,  i_2);
    cv::imshow("image", image);
    cv::imshow("i_1",  i_1);
    cv::imshow("i_2",  i_2);
    cv::waitKey();
    cv::destroyAllWindows();
    cv::Scharr(image, k_1, CV_16S, 1, 0);
    cv::Scharr(image, k_2, CV_16S, 0, 1);
    cv::convertScaleAbs(i_1,  i_1);
    cv::convertScaleAbs(i_2,  i_2);
    cv::imshow("image", image);
    cv::imshow("i_1",  i_1);
    cv::imshow("i_2",  i_2);
    cv::waitKey();
    cv::destroyAllWindows();
    cv::Canny(image, k_1, 100, 200, 3);
    cv::Canny(image, k_2, 20, 40, 3);
    cv::imshow("image", image);
    cv::imshow("i_1",  i_1);
    cv::imshow("i_2",  i_2);
    cv::waitKey();
    return 0;
}