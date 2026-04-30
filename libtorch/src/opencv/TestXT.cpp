#include "opencv2/opencv.hpp"

#include <vector>

int main() {
    auto color = cv::imread("D:/tmp/bike.jpg");
    auto image = cv::imread("D:/tmp/bike.jpg", cv::IMREAD_GRAYSCALE);
    cv::threshold(image, image, 127, 255, cv::THRESH_BINARY_INV);
    cv::Mat dst;
    // 连通
    // cv::distanceTransform(image, dst, cv::DIST_L1, cv::DIST_MASK_5, CV_8U);
    // cv::distanceTransform(image, dst, cv::DIST_L2, cv::DIST_MASK_5, CV_8U);
    // cv::distanceTransform(image, dst, cv::DIST_L1, cv::DIST_MASK_3, CV_8U);
    cv::distanceTransform(image, dst, cv::DIST_L2, cv::DIST_MASK_3, CV_8U);
    cv::imshow("image", image);
    cv::imshow("dst",   dst);
    cv::waitKey();
    const int count = cv::connectedComponents(image, dst, 8, CV_16U);
    // cv::connectedComponentsWithStats
    std::cout << "当前总类：" << count << '\n';
    std::vector<cv::Vec3b> colors(count);
    cv::RNG rng;
    for(int i = 0; i < count; ++i) {
        colors[i] = {
            static_cast<uchar>(rng.uniform(0, 255)),
            static_cast<uchar>(rng.uniform(0, 255)),
            static_cast<uchar>(rng.uniform(0, 255))
        };
    }
    for(int row = 0; row < dst.rows; ++row) {
        for(int col = 0; col < dst.cols; ++col) {
            if(dst.at<uint16_t>(row, col) == 0) {
                continue;
            }
            color.at<cv::Vec3b>(row, col) = colors[dst.at<uint16_t>(row, col)];
        }
    }
    cv::imshow("color", color);
    cv::imshow("dst",   dst);
    cv::waitKey();
    // 形态学：腐蚀、膨胀
    // 腐蚀
    cv::destroyAllWindows();
    cv::erode(image, dst, cv::getStructuringElement(0, cv::Size(3, 3)));
    // cv::erode(image, dst, cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::imshow("color", image);
    cv::imshow("dst",   dst);
    cv::waitKey();
    // 膨胀
    cv::destroyAllWindows();
    cv::dilate(image, dst, cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::imshow("color", image);
    cv::imshow("dst",   dst);
    cv::waitKey();
    // 形态学应用：开运算、闭运算、形态学梯度、顶帽运算、黑帽运算
    cv::destroyAllWindows();
    cv::Mat open, close, gradient, tophat, blackhat, hitmiss;
    cv::morphologyEx(image, open,     cv::MORPH_OPEN,     cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::morphologyEx(image, close,    cv::MORPH_CLOSE,    cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::morphologyEx(image, gradient, cv::MORPH_GRADIENT, cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::morphologyEx(image, tophat,   cv::MORPH_TOPHAT,   cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::morphologyEx(image, blackhat, cv::MORPH_BLACKHAT, cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::morphologyEx(image, hitmiss,  cv::MORPH_HITMISS,  cv::getStructuringElement(1, cv::Size(3, 3)));
    cv::imshow("image",    image);
    cv::imshow("open",     open);
    cv::imshow("close",    close);
    cv::imshow("gradient", gradient);
    cv::imshow("tophat",   tophat);
    cv::imshow("blackhat", blackhat);
    cv::imshow("hitmiss",  hitmiss);
    cv::waitKey();
    // 图像细化
    cv::destroyAllWindows();
    // cv::ximgproc::thinning
    return 0;
}
