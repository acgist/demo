#include <iostream>

#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
// #include "opencv2/objdetect.hpp"

int main() {
    cv::Mat img = cv::imread("D:/tmp/bike.jpg");
    cv::imshow("bike1", img);
    // cv::waitKey(0);
    int zoom = 2;
    int width  = img.size().width;
    int height = img.size().height;
    std::cout << img.size() << '\n';
    img = img.t();
    for(int i = 0; i < width / zoom; ++i) {
        // img.row(i) = img.row(zoom * i);
        img.row(zoom * i).copyTo(img.row(i));
        // img.row(i) = img.row(zoom * i) + cv::Scalar(0, 0, 0, 0);
    }
    img.pop_back(width / zoom * (zoom - 1));
    img = img.t();
    std::cout << img.size() << '\n';
    for(int i = 0; i < height / zoom; ++i) {
        // img.row(i) = img.row(zoom * i);
        img.row(zoom * i).copyTo(img.row(i));
        // img.row(i) = img.row(zoom * i) + cv::Scalar(0, 0, 0, 0);
    }
    img.pop_back(height / zoom * (zoom - 1));
    std::cout << img.size() << '\n';
    cv::imshow("bike2", img);
    cv::waitKey(0);
    img.release();
    return 0;
}