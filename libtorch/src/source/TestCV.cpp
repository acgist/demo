#include <iostream>

#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"

#include "opencv2/core/utils/logger.hpp"

int main() {
    cv::utils::logging::setLogLevel(cv::utils::logging::LOG_LEVEL_WARNING);
    try {
        cv::Mat image = cv::imread("D:/tmp/F4.jpg");
        cv::imshow("Image", image);
        cv::waitKey(0);
        cv::destroyWindow("Image");
        cv::Mat target;
        // 平移变换
        // cv::Mat mapping = (cv::Mat_<double>(2, 3) << 1, 0, 100, 0, 1, 300);
        double data[] = {1, 0, 100, 0, 1, 100};
        cv::Mat mapping(2, 3, CV_64F, data);
        cv::warpAffine(image, target, mapping, image.size());
        cv::imshow("Image", target);
        cv::waitKey(0);
        cv::destroyWindow("Image");
        target.release();
        mapping.release();
        // 旋转变换
        mapping = cv::getRotationMatrix2D(cv::Point2f(image.rows / 2, image.cols / 2), 45, 1);
        cv::warpAffine(image, target, mapping, image.size());
        cv::imshow("Image", target);
        cv::waitKey(0);
        cv::destroyWindow("Image");
        target.release();
        mapping.release();
        // 缩放变换
        mapping = cv::getRotationMatrix2D(cv::Point2f(image.rows / 2, image.cols / 2), 0, 0.5);
        cv::warpAffine(image, target, mapping, image.size());
        cv::imshow("Image", target);
        cv::waitKey(0);
        cv::destroyWindow("Image");
        target.release();
        mapping.release();
        // 扭曲变换
        cv::Point2f src[3] = {
            cv::Point2f(0, 0),
            cv::Point2f(image.cols, 0),
            cv::Point2f(image.cols, image.rows)
            // cv::Point2f(0, 0),
            // cv::Point2f(image.cols, 0),
            // cv::Point2f(0, image.rows)
        };
        cv::Point2f dst[3] = {
            cv::Point2f(image.cols * 0.00F, image.rows * 0.00F),
            cv::Point2f(image.cols * 0.10F, image.rows * 0.90F),
            cv::Point2f(image.cols * 1.00F, image.rows * 1.00F)
            // cv::Point2f(image.cols * 0.00F, image.rows * 0.30F),
            // cv::Point2f(image.cols * 0.85F, image.rows * 0.25F),
            // cv::Point2f(image.cols * 0.15F, image.rows * 0.70F)
        };
        mapping = cv::getAffineTransform(src, dst);
        cv::warpAffine(image, target, mapping, image.size());
        cv::Mat mergeMat(image.rows, image.cols + target.cols, image.type());
        cv::Mat submat = mergeMat.colRange(0, image.cols);
        image.copyTo(submat);
        submat = mergeMat.colRange(image.cols, image.cols + target.cols);
        target.copyTo(submat);
        cv::imshow("Image", mergeMat);
        cv::waitKey(0);
        cv::destroyWindow("Image");
        target.release();
        mapping.release();
    } catch(const std::exception& e) {
        std::cerr << e.what() << '\n';
    }
    return 0;
}
