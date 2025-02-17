#include "opencv2/opencv.hpp"

int main() {
    // 图片修复
    cv::destroyAllWindows();
    auto lena     = cv::imread("D:/tmp/lena.png");
    auto lenaLine = cv::imread("D:/tmp/lena-line.png");
    cv::imshow("lena",     lena);
    cv::imshow("lenaLine", lenaLine);
    cv::Mat lenaGray, lenaLineGray;
    cv::cvtColor(lena,     lenaGray,     cv::COLOR_RGB2GRAY, 0);
    cv::cvtColor(lenaLine, lenaLineGray, cv::COLOR_RGB2GRAY, 0);
    cv::Mat lenaMask, lenaLineMask;
    cv::threshold(lenaGray,     lenaMask,     230, 255, cv::THRESH_BINARY);
    cv::threshold(lenaLineGray, lenaLineMask, 230, 255, cv::THRESH_BINARY);
    cv::Mat kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(3, 3));
    cv::dilate(lenaMask,     lenaMask,     kernel);
    cv::dilate(lenaLineMask, lenaLineMask, kernel);
    cv::Mat lenaInpaint, lenaLineInpaint;
    cv::inpaint(lena,     lenaMask,     lenaInpaint,     5, cv::INPAINT_NS);
    cv::inpaint(lenaLine, lenaLineMask, lenaLineInpaint, 5, cv::INPAINT_NS);
    cv::imshow("lenaMask",     lenaMask);
    cv::imshow("lenaLineMask", lenaLineMask);
    cv::imshow("lenaInpaint",     lenaInpaint);
    cv::imshow("lenaLineInpaint", lenaLineInpaint);
    cv::waitKey();
    return 0;
}