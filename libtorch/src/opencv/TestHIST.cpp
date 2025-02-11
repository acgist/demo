#include "opencv2/opencv.hpp"

int main() {
    auto mat { cv::imread("D:/tmp/xxx.jpg") };
    cv::Mat hist;
    const int channels[1] = { 0   }; // 通道索引
    const int histSize[1] = { 256 }; // 直方图的维度
    const float  ranges_a[2] = { 0, 255   };
    const float* ranges_b[1] = { ranges_a }; // 像素灰度范围
    std::vector<cv::Mat> mats(3);
    cv::split(mat, mats);
    auto& gray = mats[0];
    cv::imshow("gray", gray);
    cv::waitKey();
    // 直方图均衡化
    cv::equalizeHist(gray, gray);
    cv::imshow("gray", gray);
    cv::waitKey();
    cv::calcHist(&mat, 1, channels, cv::Mat{}, hist, 1, histSize, ranges_b);
    // cv::calcHist(&gray, 1, channels, cv::Mat{}, hist, 1, histSize, ranges_b);
    // double max_val;
	// cv::minMaxLoc(hist, 0, &max_val, 0, 0);
    cv::normalize(hist, hist, 1, cv::NORM_L1);
    // cv::normalize(hist, hist, 1, cv::NORM_MINMAX);
    const int w = 512 + 32 * 2;
    const int h = 256 + 64;
    cv::Mat image(h, w, CV_8UC3);
    for(int i = 0; i < hist.rows; ++i) {
        cv::rectangle(image, cv::Rect(i * 2 + 32, 0, 2, hist.at<float>(i) * 256), cv::Scalar(0, 0, i));
        // cv::rectangle(image, cv::Rect(i * 2 + 32, 0, 2, hist.at<float>(i) / max_val * 256), cv::Scalar(0, 0, i));
    }
    cv::imshow("hist", image);
    cv::waitKey();
    double cr = cv::compareHist(hist, hist, cv::HISTCMP_KL_DIV);
    // double cr = cv::compareHist(hist, hist, cv::HISTCMP_CORREL);
    // double cr = cv::compareHist(hist, hist, cv::HISTCMP_CHISQR);
    // double cr = cv::compareHist(hist, hist, cv::HISTCMP_INTERSECT);
    // double cr = cv::compareHist(hist, hist, cv::HISTCMP_BHATTACHARYYA);
    std::cout << "cr = " << cr << '\n';
    return 0;
}

