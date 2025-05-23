#include "lifuren/Test.hpp"

#include "opencv2/opencv.hpp"

#include "lifuren/File.hpp"
#include "lifuren/Torch.hpp"
#include "lifuren/Dataset.hpp"

[[maybe_unused]] static void testXML() {
    auto source = lifuren::file::join({ lifuren::config::CONFIG.tmp, "music.xml" }).string();
    lifuren::dataset::score::Score score = lifuren::dataset::score::load_xml(source);
    for(const auto& [k, notes] : score.partMap["P1"]) {
        for(const auto& note : notes) {
            SPDLOG_DEBUG("音符：{} - {} - {} - {} - {}", k, note.step, note.alter, note.octave, note.finger);
        }
    }
    auto target = lifuren::file::join({ lifuren::config::CONFIG.tmp, "music.copy.xml" }).string();
    lifuren::dataset::score::save_xml(1, source, target, {
        { 1, 2, 3 },
        { 3, 2, 1 }
    });
}

[[maybe_unused]] static void testToPcm() {
    lifuren::dataset::audio::toPcm(lifuren::file::join({lifuren::config::CONFIG.tmp, "tts.mp3"}).string());
    // lifuren::dataset::audio::toPcm(lifuren::file::join({lifuren::config::CONFIG.tmp, "lifuren", "audio.aac"}).string());
    // lifuren::dataset::audio::toPcm(lifuren::file::join({lifuren::config::CONFIG.tmp, "lifuren", "audio.mp3"}).string());
    // lifuren::dataset::audio::toPcm(lifuren::file::join({lifuren::config::CONFIG.tmp, "lifuren", "audio.flac"}).string());
}

[[maybe_unused]] static void testToFile() {
    lifuren::dataset::audio::toFile(lifuren::file::join({lifuren::config::CONFIG.tmp, "lifuren", "audio.pcm"}).string());
}

[[maybe_unused]] static void testStft() {
    std::ifstream input;
    std::ofstream output;
    input.open (lifuren::file::join({ lifuren::config::CONFIG.tmp, "baicai.pcm"        }).string(), std::ios_base::binary);
    output.open(lifuren::file::join({ lifuren::config::CONFIG.tmp, "baicai_target.pcm" }).string(), std::ios_base::binary);
    std::vector<short> data;
    data.resize(LFR_AUDIO_PCM_LENGTH);
    while(input.read(reinterpret_cast<char*>(data.data()), LFR_AUDIO_PCM_LENGTH * sizeof(short))) {
        // auto tensor = lifuren::dataset::audio::pcm_stft(data, 400, 40, 400);
        auto tensor = lifuren::dataset::audio::pcm_stft(data, 400, 80, 400);
        // auto tensor  = lifuren::dataset::audio::pcm_stft(data, 400, 100, 400);
        // auto real    = torch::view_as_real(tensor);
        // auto complex = torch::view_as_complex(real);
        // lifuren::logTensor("tensor size", tensor.sizes());
        // lifuren::logTensor("tensor size", real.sizes());
        // lifuren::logTensor("tensor size", complex.sizes());
        // lifuren::logTensor("tensor", tensor);
        // lifuren::logTensor("tensor", real);
        // lifuren::logTensor("tensor", complex);
        // lifuren::logTensor("tensor", norm->forward(tensor));
        // auto pcm = lifuren::dataset::audio::pcm_istft(tensor, 400, 40, 400);
        // cv::Mat image(201, 56, CV_8UC1, reinterpret_cast<char*>(tensor.data_ptr()));
        // image = image.t();
        // cv::imshow("image", image);
        // cv::waitKey();
        // auto pcm = lifuren::dataset::audio::pcm_istft(tensor, 400, 40, 400);
        auto pcm = lifuren::dataset::audio::pcm_istft(tensor, 400, 80, 400);
        // auto pcm = lifuren::dataset::audio::pcm_istft(tensor, 400, 100, 400);
        output.write(reinterpret_cast<char*>(pcm.data()), pcm.size() * sizeof(short));
    }
    input.close();
    output.close();
}

[[maybe_unused]] static void testImage() {
    auto image { cv::imread(lifuren::file::join({ lifuren::config::CONFIG.tmp, "image.png" }).string()) };
    cv::imshow("image", image);
    cv::waitKey();
    lifuren::dataset::image::resize(image, 640, 480);
    auto tensor = lifuren::dataset::image::mat_to_tensor(image);
    cv::Mat target(480, 640, CV_8UC3);
    lifuren::dataset::image::tensor_to_mat(target, tensor);
    cv::imshow("target", target);
    cv::waitKey();
    cv::destroyAllWindows();
}

[[maybe_unused]] static void testStaff() {
    auto image { cv::imread(lifuren::file::join({ lifuren::config::CONFIG.tmp, "staff", "00.png" }).string()) };
    lifuren::dataset::image::resize_staff(image, 640, 1280);
    cv::imshow("image", image);
    cv::waitKey(0);
    auto images = lifuren::dataset::image::staff_slice(image);
}

[[maybe_unused]] static void testScore() {
    auto ret = lifuren::dataset::score::load_finger(lifuren::file::join({ lifuren::config::CONFIG.tmp, "PianoFingeringDataset_v1.2", "FingeringFiles", "001-1_fingering.txt" }).string());
    SPDLOG_DEBUG("数据长度：{}", ret.size());
}

[[maybe_unused]] static void testEmbeddingShikuang() {
    lifuren::dataset::allDatasetPreprocess(
        lifuren::file::join({lifuren::config::CONFIG.tmp, "shikuang"}).string(),
        lifuren::config::LIFUREN_EMBEDDING_FILE,
        &lifuren::dataset::audio::embedding_shikuang
    );
}

[[maybe_unused]] static void testLoadChopinDatasetLoader() {
}

[[maybe_unused]] static void testLoadMozartDatasetLoader() {
    auto loader = lifuren::dataset::score::loadMozartDatasetLoader(
        100,
        lifuren::file::join({
            lifuren::config::CONFIG.tmp,
            "mozart",
            "train"
        }).string()
    );
    lifuren::logTensor("曲谱特征数量", loader->begin()->data.sizes());
    lifuren::logTensor("曲谱标签数量", loader->begin()->target.sizes());
    SPDLOG_INFO("批次数量：{}", std::distance(loader->begin(), loader->end()));
    lifuren::logTensor("曲谱特征", loader->begin()->data[0]);
    lifuren::logTensor("曲谱标签1", loader->begin()->target[0]);
    lifuren::logTensor("曲谱标签2", loader->begin()->target[1]);
    lifuren::logTensor("曲谱标签3", loader->begin()->target[2]);
    lifuren::logTensor("曲谱标签4", loader->begin()->target[3]);
    lifuren::logTensor("曲谱标签5", loader->begin()->target[4]);
}

[[maybe_unused]] static void testLoadShikuangDatasetLoader() {
    auto loader = lifuren::dataset::audio::loadShikuangDatasetLoader(
        200,
        lifuren::file::join({
            lifuren::config::CONFIG.tmp,
            "shikuang",
            "train"
        }).string()
    );
    lifuren::logTensor("音频特征数量", loader->begin()->data.sizes());
    lifuren::logTensor("音频标签数量", loader->begin()->target.sizes());
    // SPDLOG_INFO("批次数量：{}", std::distance(loader->begin(), loader->end()));
    cv::Mat mat(LFR_IMAGE_HEIGHT, LFR_IMAGE_WIDTH, CV_8UC3);
    auto iterator = loader->begin();
    const int length = iterator->data.sizes()[0];
    std::ofstream output;
    output.open(lifuren::file::join({ lifuren::config::CONFIG.tmp, "shikuang_dataset.pcm" }).string(), std::ios_base::binary);
    for(; iterator != loader->end(); ++iterator) {
        for(int i = 0; i < length; ++i) {
            auto tensor = iterator->data[i];
            auto pcm = lifuren::dataset::audio::pcm_istft(tensor);
            output.write(reinterpret_cast<char*>(pcm.data()), pcm.size() * sizeof(short));
        }
    }
    output.close();
}

[[maybe_unused]] static void testLoadClassifyDatasetLoader() {
    auto loader = lifuren::dataset::image::loadClassifyDatasetLoader(
        200, 200, 5,
        lifuren::file::join({
            lifuren::config::CONFIG.tmp,
            "gender",
            "train"
        }).string(),
        {
            { "man",   1.0F },
            { "woman", 0.0F }
        }
    );
    lifuren::logTensor("图片特征数量", loader->begin()->data.sizes());
    lifuren::logTensor("图片标签数量", loader->begin()->target.sizes());
    // SPDLOG_INFO("批次数量：{}", std::distance(loader->begin(), loader->end()));
}

LFR_TEST(
    // testXML();
    // testToPcm();
    // testToFile();
    // testStft();
    // testImage();
    // testStaff();
    // testScore();
    // testEmbeddingShikuang();
    // testLoadChopinDatasetLoader();
    // testLoadMozartDatasetLoader();
    testLoadShikuangDatasetLoader();
    // testLoadClassifyDatasetLoader();
);
