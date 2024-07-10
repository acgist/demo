#include "../header/OpenCV.hpp"

#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"

void lifuren::resize(const std::string& path) {
    cv::Mat image = cv::imread(path);
    cv::Mat imageCrop;
    cv::Mat imageResize;
    cv::resize(image, imageResize, cv::Size(), 0.5, 0.5);
    cv::Rect crop(200, 200, 100, 100);
    imageCrop = image(crop);
    cv::imshow("Image", image);
    cv::imshow("ImageCrop", imageCrop);
    cv::imshow("ImageResize", imageResize);
    cv::waitKey(0);
    cv::destroyWindow("Image");
    cv::destroyWindow("ImageCrop");
    cv::destroyWindow("ImageResize");
    image.release();
    imageCrop.release();
    imageResize.release();
}

int main(const int argc, const char * const argv[]) {
    lifuren::resize("D:/tmp/Dota2.jpg");
    return 0;
}
