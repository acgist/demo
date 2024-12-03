#include "opencv2/opencv.hpp"

#include <iostream>

int main() {
    cv::VideoCapture audio("D:/tmp/264.mp4");
    // cv::VideoCapture audio("D:/tmp/audio.mono.mp3");
    if(!audio.isOpened()) {
        return -1;
    }
    std::cout << "channels = " << audio.get(cv::CAP_PROP_AUDIO_TOTAL_CHANNELS)     << '\n';
    std::cout << "samples  = " << audio.get(cv::CAP_PROP_AUDIO_SAMPLES_PER_SECOND) << '\n';
    return 0;
}
