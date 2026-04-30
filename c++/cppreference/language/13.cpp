#include <vector>
#include <iostream>

void print(const std::vector<int>& vector) {
    for(const auto& v : vector) {
        std::cout << v << " ";
    }
}

int main() {
    int batch_size = 2;
    std::vector<int> audio;
    std::vector<int> video;
    std::vector<int> label;
    std::vector<int> audio_{ 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
    std::vector<int> video_{ 20, 21, 22, 23, 24, 25, 26, 27, 28, 29 };
    audio.assign(audio_.begin(), audio_.begin() + batch_size);
    video.assign(video_.begin(), video_.begin() + batch_size);
    label.assign(audio_.begin() + 1, audio_.begin() + 1 + batch_size);
    audio_.erase(audio_.begin(), audio_.begin() + batch_size);
    video_.erase(video_.begin(), video_.begin() + batch_size);
    std::cout << "audio = ";
    print(audio);
    std::cout << std::endl;
    std::cout << "video = ";
    print(video);
    std::cout << std::endl;
    std::cout << "label = ";
    print(label);
    std::cout << std::endl;
    std::cout << "audio_ = ";
    print(audio_);
    std::cout << std::endl;
    std::cout << "video_ = ";
    print(video_);
    std::cout << std::endl;
    audio.assign(audio_.begin(), audio_.begin() + batch_size);
    video.assign(video_.begin(), video_.begin() + batch_size);
    label.assign(audio_.begin() + 1, audio_.begin() + 1 + batch_size);
    audio_.erase(audio_.begin(), audio_.begin() + batch_size);
    video_.erase(video_.begin(), video_.begin() + batch_size);
    std::cout << "audio = ";
    print(audio);
    std::cout << std::endl;
    std::cout << "video = ";
    print(video);
    std::cout << std::endl;
    std::cout << "label = ";
    print(label);
    std::cout << std::endl;
    std::cout << "audio_ = ";
    print(audio_);
    std::cout << std::endl;
    std::cout << "video_ = ";
    print(video_);
    std::cout << std::endl;
    return 0;
}