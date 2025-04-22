#include "lifuren/Dataset.hpp"

#include <map>
#include <iostream>

#include "tinyxml2.h"

#include "spdlog/spdlog.h"

#include "lifuren/File.hpp"
#include "lifuren/Torch.hpp"
#include "lifuren/String.hpp"

static std::map<std::string, int> step_map{
    {"C",   1},
    {"C#",  2},
    {"Db",  2},
    {"D",   3},
    {"D#",  4},
    {"Eb",  4},
    {"E",   5},
    {"F",   6},
    {"F#",  7},
    {"Gb",  7},
    {"G",   8},
    {"G#",  9},
    {"Ab",  9},
    {"A",  10},
    {"A#", 11},
    {"Bb", 11},
    {"B",  12},
};

static void parse_part   (tinyxml2::XMLElement*, lifuren::dataset::score::Score&);
static void parse_measure(tinyxml2::XMLElement*, std::map<int, std::vector<lifuren::dataset::score::Note>>&);
static void parse_note   (tinyxml2::XMLElement*, std::vector<lifuren::dataset::score::Note>&);

const static int feature_size = 5;
const static int feature_dims = 27;

lifuren::dataset::score::Score lifuren::dataset::score::load_xml(const std::string& path) {
    tinyxml2::XMLDocument doc;
    lifuren::dataset::score::Score score;
    score.file_path = path;
    if(doc.LoadFile(path.c_str()) != tinyxml2::XMLError::XML_SUCCESS) {
        SPDLOG_WARN("打开文件失败：{}", path);
        return score;
    }
    SPDLOG_DEBUG("打开文件：{}", path);
    auto root = doc.RootElement();
    auto part = root->FirstChildElement("part");
    while(part) {
        ::parse_part(part, score);
        part = part->NextSiblingElement("part");
    }
    return score;
}

bool lifuren::dataset::score::save_xml(const int right_staff, const std::string& source, const std::string& target, const std::vector<std::vector<int>>& fingerings) {
    if(fingerings.empty()) {
        SPDLOG_WARN("保存文件失败：{}", source);
        return false;
    }
    tinyxml2::XMLDocument doc;
    if(doc.LoadFile(source.c_str()) != tinyxml2::XMLError::XML_SUCCESS) {
        SPDLOG_WARN("打开文件失败：{}", source);
        return false;
    }
    auto left_iter  = fingerings.begin()->begin();
    auto left_end   = fingerings.begin()->end();
    auto right_iter = fingerings.rbegin()->begin();
    auto right_end  = fingerings.rbegin()->end();
    auto root = doc.RootElement();
    auto part = root->FirstChildElement("part");
    while(part) {
        auto measure = part->FirstChildElement("measure");
        while(measure) {
            auto note = measure->FirstChildElement("note");
            while(note) {
                int staff_value = 1;
                auto staff = note->FirstChildElement("staff");
                if(staff) {
                    staff_value = std::atoi(staff->GetText());
                }
                auto pitch = note->FirstChildElement("pitch");
                if(pitch) {
                    if(right_staff == staff_value) {
                        if(right_iter != right_end) {
                            note->InsertNewChildElement("notations")->InsertNewChildElement("technical")->InsertNewChildElement("fingering")->SetText(*right_iter);
                            ++right_iter;
                        }
                    } else {
                        if(left_iter != left_end) {
                            note->InsertNewChildElement("notations")->InsertNewChildElement("technical")->InsertNewChildElement("fingering")->SetText(*left_iter);
                            ++left_iter;
                        }
                    }
                }
                note = note->NextSiblingElement("note");
            }
            measure = measure->NextSiblingElement("measure");
        }
        part = part->NextSiblingElement("part");
    }
    if(doc.SaveFile(target.c_str()) == tinyxml2::XMLError::XML_SUCCESS) {
        SPDLOG_DEBUG("保存文件：{}", target);
        return true;
    } else {
        SPDLOG_WARN("保存文件失败：{}", target);
        return false;
    }
}

static std::vector<std::vector<lifuren::dataset::score::Finger>> load_from_xml(const std::string&);
static std::vector<std::vector<lifuren::dataset::score::Finger>> load_from_txt(const std::string&);

std::vector<std::vector<lifuren::dataset::score::Finger>> lifuren::dataset::score::load_finger(const std::string& path) {
    const auto suffix = lifuren::file::file_suffix(path);
    if(suffix == ".txt") {
        return load_from_txt(path);
    } else if(suffix == ".xml" || suffix == ".musicxml") {
        return load_from_xml(path);
    } else {
        SPDLOG_WARN("不支持的文件格式：{}", path);
        return {};
    }
}

inline void fill_dataset(
    size_t batch_size,
    std::vector<int>& classify_size,
    const torch::DeviceType   & device,
    std::vector<torch::Tensor>& labels,
    std::vector<torch::Tensor>& features,
    std::vector<std::vector<torch::Tensor>>& labels_tensors,
    std::vector<std::vector<torch::Tensor>>& features_tensors
) {
    size_t sum = 0;
    size_t max = 0;
    size_t min = std::numeric_limits<size_t>::max();
    for(const auto& value : features_tensors) {
        sum += value.size();
        max = std::max(max, value.size());
        min = std::min(min, value.size());
    }
    size_t avg_length = sum / labels_tensors.size();
    const static auto default_none_label   = torch::tensor({ 1, 0, 0, 0, 0, 0           }, torch::kFloat32).to(lifuren::getDevice());
    const static auto default_none_feature = torch::zeros ({ feature_size, feature_dims }, torch::kFloat32).to(lifuren::getDevice());
    for (size_t i = 0; i < avg_length; i++) {
        for(size_t j = 0; j < batch_size; ++j) {
            if(j < labels_tensors.size()) {
                const auto& value = labels_tensors[j];
                if(i < value.size()) {
                    labels.push_back(value[i]);
                } else {
                    labels.push_back(default_none_label);
                }
            } else {
                labels.push_back(default_none_label);
            }
            classify_size[labels.back().argmax(0).item<int>()] += 1;
        }
        for(size_t j = 0; j < batch_size; ++j) {
            if(j < features_tensors.size()) {
                const auto& value = features_tensors[j];
                if(i < value.size()) {
                    features.push_back(value[i]);
                } else {
                    features.push_back(default_none_feature);
                }
            } else {
                features.push_back(default_none_feature);
            }
        }
    }
    labels_tensors  .clear();
    features_tensors.clear();
}

lifuren::dataset::SeqDatasetLoader lifuren::dataset::score::loadMozartDatasetLoader(const size_t batch_size, const std::string& path) {
    std::vector<int> classify_size(6, 0);
    std::vector<std::vector<torch::Tensor>> labels_tensors;
    std::vector<std::vector<torch::Tensor>> features_tensors;
    auto dataset = lifuren::dataset::Dataset(
        path,
        { ".txt", ".xml", ".musicxml" },
        [batch_size, &classify_size, &labels_tensors, &features_tensors] (
            const std::string         & file,
            std::vector<torch::Tensor>& labels,
            std::vector<torch::Tensor>& features,
            const torch::DeviceType   & device
        ) {
            const auto vectors = lifuren::dataset::score::load_finger(file);
            for(const auto& vector : vectors) {
                if(vector.empty()) {
                    continue;
                }
                std::vector<float> label(6, 0.0F);
                std::vector<float> feature(feature_dims * feature_size, 0.0F); // 当前 + 后四
                std::vector<torch::Tensor> label_tensors;
                std::vector<torch::Tensor> feature_tensors;
                for(size_t i = 0; i < vector.size(); ++i) {
                    const auto value = vector[i];
                    std::fill(label.begin(),   label.end(),   0.0F);
                    std::fill(feature.begin(), feature.end(), 0.0F);
                    label[value.finger] = 1.0F;
                    for(int j = 0; j < 5; ++j) {
                        if(i + j >= vector.size()) {
                            continue;
                        }
                        const auto next = vector[i + j];
                        feature[j * feature_dims + next.hand]        = 1.0F;
                        feature[j * feature_dims + next.step   +  1] = 1.0F;
                        feature[j * feature_dims + next.octave + 14] = 1.0F;
                        feature[j * feature_dims + 24] = 1.0F * next.step;
                        feature[j * feature_dims + 25] = 1.0F * next.octave;
                        feature[j * feature_dims + 26] = 1.0F * (next.octave * 12 + next.step);
                    }
                    label_tensors  .push_back(torch::from_blob(label.data(),   { 6                          }, torch::kFloat32).clone().to(device));
                    feature_tensors.push_back(torch::from_blob(feature.data(), { feature_size, feature_dims }, torch::kFloat32).clone().to(device));
                }
                labels_tensors  .push_back(std::move(label_tensors));
                features_tensors.push_back(std::move(feature_tensors));
                if(labels_tensors.size() >= batch_size) {
                    fill_dataset(batch_size, classify_size, device, labels, features, labels_tensors, features_tensors);
                }
            }
        },
        [batch_size, &classify_size, &labels_tensors, &features_tensors] (
            std::vector<torch::Tensor>& labels,
            std::vector<torch::Tensor>& features,
            const torch::DeviceType   & device
        ) {
            if(!labels_tensors.empty()) {
                fill_dataset(batch_size, classify_size, device, labels, features, labels_tensors, features_tensors);
            }
        }
    ).map(torch::data::transforms::Stack<>());
    for(const auto& size : classify_size) {
        SPDLOG_DEBUG("分类数量：{}", size);
    }
    return torch::data::make_data_loader<LFT_SEQ_SAMPLER>(std::move(dataset), batch_size);
}

static std::vector<std::vector<lifuren::dataset::score::Finger>> load_from_xml(const std::string& file) {
    auto score = lifuren::dataset::score::load_xml(file);
    if(score.partMap.size() != 1) {
        SPDLOG_DEBUG("不支持的乐谱格式：{} - {}", file, score.partMap.size());
        return {};
    }
    auto staff_map = score.partMap.begin()->second;
    if(staff_map.size() != 2) {
        SPDLOG_DEBUG("不支持的乐谱格式：{} - {}", file, staff_map.size());
        return {};
    }
    std::vector<lifuren::dataset::score::Finger> fingers_r;
    std::vector<lifuren::dataset::score::Finger> fingers_l;
    fingers_r.reserve(1024);
    fingers_l.reserve(1024);
    const int max = std::max(staff_map.begin()->first, staff_map.rbegin()->first);
    for(const auto& [k, notes] : staff_map) {
        for(const auto& note : notes) {
            lifuren::dataset::score::Finger finger;
            auto step_iter = step_map.find(std::string(1, note.step));
            if(step_iter == step_map.end()) {
                SPDLOG_WARN("不支持的符号：{}", note.step);
                return {};
            }
            if(note.finger < 1 || note.finger > 5) {
                SPDLOG_DEBUG("指法标记错误：{} - {} - {}", note.step, note.octave, note.finger);
                continue;
            }
            finger.step   = step_iter->second + note.alter;
            finger.octave = note.octave;
            if(finger.step < 1) {
                finger.step   += 12;
                finger.octave -= 1;
            } else if(finger.step > 12) {
                finger.step   -= 12;
                finger.octave += 1;
            }
            finger.finger = note.finger;
            if(k == max) {
                finger.hand = 0;
                fingers_l.push_back(std::move(finger));
            } else {
                finger.hand = 1;
                fingers_r.push_back(std::move(finger));
            }
        }
    }
    return {fingers_l, fingers_r};
}

static std::vector<std::vector<lifuren::dataset::score::Finger>> load_from_txt(const std::string& file) {
    std::ifstream stream;
    stream.open(file);
    if(!stream.is_open()) {
        SPDLOG_WARN("文件打开失败：{}", file);
        return {};
    }
    std::string line;
    std::vector<lifuren::dataset::score::Finger> fingers_r;
    std::vector<lifuren::dataset::score::Finger> fingers_l;
    fingers_r.reserve(1024);
    fingers_l.reserve(1024);
    while(std::getline(stream, line)) {
        if(line.empty()) {
            continue;
        }
        auto vector = lifuren::string::split(line, std::vector<std::string>{ " ", "\t" });
        if(vector.size() != 8) {
            continue;
        }
        auto pitch = vector[3];
        int pitch_length = pitch.length();
        auto step_iter = step_map.find(pitch.substr(0, pitch_length - 1));
        if(step_iter == step_map.end()) {
            SPDLOG_WARN("不支持的符号：{}", pitch);
            stream.close();
            return {};
        }
        auto finger = std::atoi(vector[7].c_str());
        if(finger < 0) {
            fingers_l.push_back({
                .hand   = 0,
                .step   = step_iter->second,
                .octave = std::atoi(pitch.substr(pitch_length - 1).c_str()),
                .finger = std::abs(finger)
            });
        } else {
            fingers_r.push_back({
                .hand   = 1,
                .step   = step_iter->second,
                .octave = std::atoi(pitch.substr(pitch_length - 1).c_str()),
                .finger = finger
            });
        }
    }
    stream.close();
    return {fingers_l, fingers_r};
}

static void parse_part(tinyxml2::XMLElement* element, lifuren::dataset::score::Score& score) {
    std::map<int, std::vector<lifuren::dataset::score::Note>> map;
    std::string id = element->Attribute("id");
    auto measure = element->FirstChildElement("measure");
    while(measure) {
        parse_measure(measure, map);
        measure = measure->NextSiblingElement("measure");
    }
    score.partMap.emplace(id, std::move(map));
}

static void parse_measure(tinyxml2::XMLElement* element, std::map<int, std::vector<lifuren::dataset::score::Note>>& map) {
    auto note = element->FirstChildElement("note");
    while(note) {
        int staff_value = 1;
        auto staff = note->FirstChildElement("staff");
        if(staff) {
            staff_value = std::atoi(staff->GetText());
        }
        auto staff_iter = map.find(staff_value);
        if(staff_iter == map.end()) {
            staff_iter = map.emplace(staff_value, std::vector<lifuren::dataset::score::Note>{}).first;
        }
        parse_note(note, staff_iter->second);
        note = note->NextSiblingElement("note");
    }
}

static void parse_note(tinyxml2::XMLElement* element, std::vector<lifuren::dataset::score::Note>& notes) {
    auto pitch = element->FirstChildElement("pitch");
    if(pitch) {
        lifuren::dataset::score::Note note;
        auto step   = pitch->FirstChildElement("step");
        auto alter  = pitch->FirstChildElement("alter");
        auto octave = pitch->FirstChildElement("octave");
        if(step) {
            note.step = step->GetText()[0];
        }
        if(alter) {
            note.alter = std::atoi(alter->GetText());
        }
        if(octave) {
            note.octave = std::atoi(octave->GetText());
        }
        auto notations = element->FirstChildElement("notations");
        if(notations) {
            auto technical = notations->FirstChildElement("technical");
            if(technical) {
                auto fingering = technical->FirstChildElement("fingering");
                note.finger = std::atoi(fingering->GetText());
            }
        }
        notes.push_back(std::move(note));
    }
}
