#include "opencv2/opencv.hpp"
#include "opencv2/core/core.hpp"

static void drawLine(
    cv::Mat& image,
    const std::vector<cv::Vec2f>& lines,
    const double rows,
    const double cols
) {
    cv::Point pt1, pt2;
    for(int i = 0; i < lines.size(); ++i) {
        float rho   = lines[i][0]; // 直线距离原点距离
        float theta = lines[i][0]; // 直线原点垂线夹角（X轴）
        double a = std::cos(theta);
        double b = std::sin(theta);
        double x0 = a * rho, y0 = b * rho;
        double length = std::max(rows, cols);
        pt1.x = std::round(x0 + length * (-b));
        pt1.y = std::round(y0 + length * a);
        pt2.x = std::round(x0 - length * (-b));
        pt2.y = std::round(y0 - length * a);
        cv::line(image, pt1, pt2, cv::Scalar(0, 255, 0), 1);
    }
}

int main() {
    auto image { cv::imread("D:/tmp/bike.jpg") };
    // auto image { cv::imread("D:/tmp/bike.jpg", cv::IMREAD_GRAYSCALE) };
    cv::Mat edge;
    cv::Canny(image, edge, 80, 180, 3, false);
    cv::threshold(edge, edge, 170, 255, cv::THRESH_BINARY);
    std::vector<cv::Vec2f> lines1, lines2;
    cv::HoughLines(edge, lines1, 1, CV_PI, 150, 0, 0);
    cv::HoughLines(edge, lines2, 1, CV_PI, 200, 0, 0);
    cv::Mat image1 = image.clone();
    cv::Mat image2 = image.clone();
    drawLine(image1, lines1, image.rows, image.cols);
    drawLine(image2, lines2, image.rows, image.cols);
    cv::imshow("image", image);
    cv::imshow("edge", edge);
    cv::imshow("image1", image1);
    cv::imshow("image2", image2);
    cv::waitKey();
    cv::destroyAllWindows();
    // 轮廓检测
    std::vector<std::vector<cv::Point>> contours;
    std::vector<cv::Vec4i> hierarchy;
    cv::findContours(edge, contours, hierarchy, 0, 2, cv::Point());
    for(int i = 0; i < contours.size(); ++i) {
        // 最大外接矩形
        // cv::Rect rect = cv::boundingRect(contours[i]);
        // cv::rectangle(image, rect, cv::Scalar(0, 255, 0));
        // 最小外接矩形
        auto rect = cv::minAreaRect(contours[i]);
        std::vector<cv::Point2f> points(4);
        rect.points(points);
        for(int j = 0; j < 4; ++j) {
            if(j == 3) {
                cv::line(image, points[j], points[0], cv::Scalar(0, 255, 0), 2, 8, 0);
            } else {
                cv::line(image, points[j], points[j + 1], cv::Scalar(0, 255, 0), 2, 8, 0);
            }
        }
    }
    cv::imshow("image", image);
    cv::waitKey();
    // 轮廓面积
    // 轮廓周长
    return 0;
}