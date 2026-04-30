#include <vector>
#include <iostream>

void print(const std::vector<int>& vector) {
    for(const auto& v : vector) {
        std::cout << v << " ";
    }
}

void print(const std::vector<std::vector<int>>& vector) {
    for(const auto& v : vector) {
        print(v);
        std::cout << std::endl;
    }
}

int main() {
    int batch_size = 2;
    int batch_wind = 3;
    std::vector<std::vector<int>> audio;
    std::vector<int> video;
    std::vector<int> label;
    std::vector<int> audio_{ 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
    std::vector<int> video_{ 20, 21, 22, 23, 24, 25, 26, 27, 28, 29 };
    for (size_t i = 0; i < batch_size; i++) {
        std::vector<int> wind(batch_wind);
        wind.assign(audio_.begin() + i, audio_.begin() + i + batch_wind);
        audio.push_back(wind);
    }
    video.assign(video_.begin() + batch_wind - 1, video_.begin() + batch_wind - 1 + batch_size);
    label.assign(audio_.begin() + batch_wind, audio_.begin() + batch_wind + batch_size);
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
    audio.clear();
    video.clear();
    label.clear();
    for (size_t i = 0; i < batch_size; i++) {
        std::vector<int> wind(batch_wind);
        wind.assign(audio_.begin() + i, audio_.begin() + i + batch_wind);
        audio.push_back(wind);
    }
    video.assign(video_.begin() + batch_wind - 1, video_.begin() + batch_wind + batch_size - 1);
    label.assign(audio_.begin() + batch_wind, audio_.begin() + batch_wind + batch_size);
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