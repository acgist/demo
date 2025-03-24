#include "opencv2/opencv.hpp"

void resize_staff(cv::Mat& image, const int width, const int height) {
    const int cols = image.cols;
    const int rows = image.rows;
    const double ws = 1.0 * width  / cols;
    const double hs = 1.0 * height / rows;
    const double scale = std::min(ws, hs);
    cv::resize(image, image, cv::Size(), scale, scale, cv::INTER_AREA);
}

std::vector<cv::Mat> staff_slice(cv::Mat& image) {
    if(image.empty()) {
        return {};
    }
    if(image.channels() != 1) {
        cv::cvtColor(image, image, cv::COLOR_BGR2GRAY);
    }
    const int thresh = cv::mean(image)[0] / 2;
    cv::threshold(image, image, thresh, 255, cv::THRESH_BINARY);
    resize_staff(image, 640, 640);
    const int cols = image.cols;
    const int rows = image.rows;
    int begin = 0;
    for(int row = 0; row < rows; ++row) {
        auto row_data = image.row(row);
        const int w_count = cv::countNonZero(row_data);
        const int b_count = cols - w_count;
        if(b_count > 0) {

        } else {
            if(row - begin > 1) {
                cv::Mat result = image(cv::Rect(0, begin, cols, row - begin));
                cv::imshow("result", result);
                // cv::waitKey(0);
            }
            begin = row;
        }
    }
    return { image };
}

int main() {
    auto image { cv::imread("D:/tmp/staff/00.png") };
    auto images = staff_slice(image);
    for(const auto mat : images) {
        cv::imshow("image", mat);
        cv::waitKey(0);
    }
}
