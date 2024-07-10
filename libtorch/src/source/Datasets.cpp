#include "../header/Datasets.hpp"

#include <numeric>
#include <iostream>
#include <algorithm>
#include <filesystem>

#include "../header/Files.hpp"

/**
 * 标准计算
 */
struct StandardizationInfo {

    float min   = 0.0F; // 最小值
    float max   = 0.0F; // 最大值
    float std   = 0.0F; // 标准差
    float mean  = 0.0F; // 均值
    float range = 0.0F; // 极差
    double var  = 0.0; // 方差
    double sum  = 0.0;  // 和

};

/**
 * 标准计算
 * 
 * @param info   结果
 * @param vector 数据
 */
static void standardizationInfo(StandardizationInfo& info, std::vector<float>& vector) {
    // 最小值
    info.min = *std::min_element(vector.begin(), vector.end());
    // 最大值
    info.max = *std::max_element(vector.begin(), vector.end());
    // 极值
    info.range = info.max - info.min;
    // 和
    // info.sum = std::reduce(vector.begin(), vector.end());
    info.sum = std::accumulate(vector.begin(), vector.end(), 0.0);
    // 均值
    info.mean = info.sum / vector.size();
    // 方差
    std::for_each(vector.begin(), vector.end(), [&info](auto& value) {
        info.var += std::pow(value - info.mean, 2);
    });
    info.var /= vector.size();
    // 标准差
    info.std = std::sqrt(info.var);
}

lifuren::datasets::FileDataset::FileDataset(
    const std::string& path,
    const std::vector<std::string>& exts,
    const std::map<std::string, int>& mapping,
    const std::function<torch::Tensor(const std::string&)> fileTransform
) : fileTransform(fileTransform) {
    if(!std::filesystem::exists(path) || !std::filesystem::is_directory(path)) {
        printf("目录无效：%s", path.data());
        return;
    }
    auto iterator = std::filesystem::directory_iterator(std::filesystem::path(path));
    for(const auto& entry : iterator) {
        std::string filepath = entry.path().string();
        if(entry.is_directory()) {
            std::string filename = entry.path().filename().string();
            const uint64_t oldSize = this->paths.size();
            lifuren::files::listFiles(this->paths, entry.path().string(), exts);
            const uint64_t newSize = this->paths.size();
            for(uint64_t index = oldSize; index < newSize; ++index) {
                this->labels.push_back(mapping.at(filename));
            }
        } else {
            printf("忽略无效文件：%s", filepath.data());
        }
    }
}

torch::optional<size_t> lifuren::datasets::FileDataset::size() const {
    return this->paths.size();
}

torch::data::Example<> lifuren::datasets::FileDataset::get(size_t index) {
    const std::string& path   = this->paths.at(index);
    torch::Tensor data = this->fileTransform(path);
    const int label = this->labels.at(index);
    torch::Tensor target = torch::full({1}, label);
    return { 
        data,
        target
    };
}

lifuren::datasets::TensorDataset::TensorDataset(torch::Tensor& features, const torch::Tensor& labels) : features(features), labels(labels) {
}

torch::optional<size_t> lifuren::datasets::TensorDataset::size() const {
    return this->features.sizes()[0];
}

torch::data::Example<> lifuren::datasets::TensorDataset::get(size_t index) {
    return {
        this->features[index],
        this->labels[index]
    };
}

/**
 * @param v 字符串
 * 
 * @return 是否数值
 */
static bool isNumber(const std::string& v) {
    return std::ranges::all_of(v.begin(), v.end(), [](auto& x) {
        return std::isdigit(x);
    });
}

std::vector<std::vector<std::string>> lifuren::datasets::loadCSV(const std::string& path) {
    std::ifstream stream;
    stream.open(path);
    if(!stream.is_open()) {
        stream.close();
        return {};
    }
    std::string line;
    int size  = 0;
    int index = 0;
    int jndex = 0;
    std::vector<std::vector<std::string>> ret;
    while(std::getline(stream, line)) {
        std::vector<std::string> data;
        data.reserve(size);
        if(line.empty()) {
            continue;
        }
        index = 0;
        jndex = 0;
        while((jndex = line.find(',', index)) != std::string::npos) {
            data.push_back(line.substr(index, jndex - index));
            index = jndex + 1;
        }
        data.push_back(line.substr(index, line.length() - index));
        ret.push_back(data);
    }
    stream.close();
    return ret;
}

std::vector<float> lifuren::datasets::mark(
    std::vector<std::vector<std::string>>& data,
    int& rows, int& cols,
    const bool ignoreUnknown,
    const bool standardization,
    const int rowStartPos,
    const int colStartPos,
    const int colEndPos
) {
    std::vector<float> ret;
    if(data.empty()) {
        return ret;
    }
    // 连续值
    static std::map<int, StandardizationInfo> lineMapping;
    // 离散值
    static std::map<int, std::vector<std::string>> pointMapping;
    rows = data.size()    - rowStartPos;
    cols = data[0].size() - colStartPos;
    // 数据数值化
    std::vector<std::vector<float>> table;
    table.reserve(rows);
    std::for_each(data.begin() + rowStartPos, data.end(), [&cols, &table, &colStartPos, &ignoreUnknown](const auto& row) {
        int colIndex = -1;
        std::vector<float> tableRow;
        tableRow.reserve(cols);
        std::for_each(row.begin() + colStartPos, row.end(), [&colIndex, &table, &tableRow, &ignoreUnknown](const auto& cell) {
            colIndex++;
            if(isNumber(cell)) {
                tableRow.push_back(std::atof(cell.c_str()));
                return;
            }
            // 离散值转数值
            if(pointMapping.find(colIndex) == pointMapping.end()) {
                pointMapping.emplace(colIndex, std::vector<std::string>{});
            }
            auto& colMapping = pointMapping.at(colIndex);
            auto colPos = std::find(colMapping.begin(), colMapping.end(), cell);
            if(colPos == colMapping.end()) {
                if(ignoreUnknown) {
                    tableRow.push_back(0);
                    std::cout << "忽略未知类型：" << colIndex << " = " << cell << '\n';
                } else {
                    colMapping.push_back(cell);
                    tableRow.push_back(colMapping.size() - 1);
                }
            } else {
                tableRow.push_back(std::distance(colMapping.begin(), colPos));
            }
        });
        table.push_back(tableRow);
    });
    // 数据标准化：可以的话提前开辟空间
    if(standardization) {
        for(int colIndex = cols - 1 - colEndPos; colIndex >= 0; --colIndex) {
            if(pointMapping.find(colIndex) != pointMapping.end()) {
                auto& colMapping = pointMapping.at(colIndex);
                const int size   = colMapping.size();
                if(size > 1) {
                    // 离散值标准化
                    std::for_each(table.begin(), table.end(), [&colIndex, &size](auto& tableRow) {
                        auto  index  = static_cast<int>(tableRow[colIndex]);
                        auto* array  = new float[size] { 0.0F };
                        array[index] = 1.0F;
                        tableRow.erase(tableRow.begin()  + colIndex);
                        tableRow.insert(tableRow.begin() + colIndex, array, array + size);
                        delete[] array;
                    });
                    continue;
                }
                // 只有一个值时当作连续值标准化
            }
            // 连续值标准化
            std::vector<float> tableCol;
            tableCol.reserve(table.size());
            std::for_each(table.begin(), table.end(), [&colIndex, &tableCol](auto& tableRow) {
                tableCol.push_back(tableRow[colIndex]);
            });
            if(lineMapping.find(colIndex) == lineMapping.end()) {
                StandardizationInfo info;
                standardizationInfo(info, tableCol);
                lineMapping.emplace(colIndex, info);
            }
            StandardizationInfo& info = lineMapping.at(colIndex);
            // 归一化
            // 1. (x - min(x))  / (max(x) - min(x))
            // 2. (x - mean(x)) / (max(x) - min(x))
            // 标准化
            // 1. (x - mean(x)) / std(x)
            std::for_each(table.begin(), table.end(), [&info, &colIndex](auto& tableRow) {
                tableRow[colIndex] = (tableRow[colIndex] - info.min) / info.range;
                // tableRow[colIndex] = (tableRow[colIndex] - info.mean) / info.range;
                // tableRow[colIndex] = (tableRow[colIndex] - info.mean) / info.std;
            });
        }
    }
    // 数据展开
    int pos = 0;
    rows = table.size();
    cols = table[0].size();
    ret.reserve(rows * cols);
    std::for_each(table.begin(), table.end(), [&pos, &ret](auto& tableRow) {
        ret.insert(ret.begin() + pos, tableRow.begin(), tableRow.end());
        pos += tableRow.size();
    });
    return ret;
}
