#include "opencv2/opencv.hpp"

int main() {
    auto image { cv::imread("D:/tmp/yang1.jpg") };
    cv::imshow("image", image);
    cv::Mat camera_matrix = (cv::Mat_<float>(3, 3) <<
        532.016297, 0, 332.172519,
        0, 531.565159, 233.388075,
        0, 0, 1
    );
    cv::Mat dist_coeffs = (cv::Mat_<float>(1, 5) <<
        -0.285188, 0.080097, 0.001274, -0.002415, 0.106579
    );
    auto R = cv::Mat::eye(3, 3, CV_32F);
    cv::Mat mapx(image.rows, image.cols, CV_32FC1);
    cv::Mat mapy(image.rows, image.cols, CV_32FC1);
    cv::initUndistortRectifyMap(camera_matrix, dist_coeffs, R, camera_matrix, cv::Size(image.cols, image.rows), CV_32FC1, mapx, mapy);
    cv::Mat image_remap;
    cv::remap(image, image_remap, mapx, mapy, cv::INTER_LINEAR);
    cv::imshow("image_remap", image_remap);
    cv::Mat image_undistort;
    cv::undistort(image, image_undistort, camera_matrix, dist_coeffs);
    cv::imshow("image_undistort", image_undistort);
    cv::waitKey();
    return 0;
}
