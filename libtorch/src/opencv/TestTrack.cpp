#include "opencv2/opencv.hpp"

int main() {
    // 均值迁移法目标跟踪（爬山算法）
    cv::VideoCapture video("D:/tmp/fun.mp4");
    int trackObject = 0;
    int hsize = 16;
    float hranges[] = { 0, 180 };
    const float* phranges = hranges;
    cv::Mat frame, hsv, hue, hist, backproj;
    cv::Rect selection, selection_cam;
    while(video.read(frame)) {
        int ch[] = { 0, 0 };
        cv::cvtColor(frame, hsv, cv::COLOR_BGR2HSV);
        hue.create(hsv.size(), hsv.depth());
        cv::mixChannels(&hsv, 1, &hue, 1, ch, 1);
        if(trackObject <= 0) {
            selection = cv::selectROI("选择目标", frame, true, false);
            selection_cam = selection;
            cv::Mat roi(hue, selection);
            // 计算直方图和归一化
            cv::calcHist(&roi, 1, 0, cv::Mat{}, hist, 1, &hsize, &phranges);
            cv::normalize(hist, hist);
            // cv::normalize(hist, hist, 0, 255, cv::NORM_MINMAX);
            trackObject = 1;
        }
        // 计算目标区域反向直方图
        cv::calcBackProject(&hue, 1, 0, hist, backproj, &phranges);
        // 均值迁移跟踪
        cv::meanShift(backproj, selection, cv::TermCriteria(cv::TermCriteria::EPS | cv::TermCriteria::COUNT, 100, 1));
        cv::rectangle(frame, selection, cv::Scalar(0, 0, 255), 3, cv::LINE_AA);
        auto clone = frame.clone();
        // 自适应
        auto trackBox = cv::CamShift(backproj, selection_cam, cv::TermCriteria(cv::TermCriteria::EPS | cv::TermCriteria::COUNT, 100, 1));
        cv::ellipse(clone, trackBox, cv::Scalar(0, 0, 255), 3, cv::LINE_AA);
        cv::imshow("frame", frame);
        cv::imshow("clone", clone);
        int c = cv::waitKey(50);
        if(c == 'e') {
            break;
        }
    }
    // 光流法
    return 0;
}