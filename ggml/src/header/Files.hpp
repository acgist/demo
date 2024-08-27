/**
 * 文件工具
 * 
 * @author acgist
 */
#ifndef LFR_HEADER_CORE_FILES_HPP
#define LFR_HEADER_CORE_FILES_HPP

#include <string>
#include <vector>
#include <filesystem>

namespace lifuren {
namespace files   {

/**
 * 遍历文件列表
 * 
 * @param vector 列表
 * @param path   路径
 */
extern void listFiles(std::vector<std::string>& vector, const std::string& path);

/**
 * 遍历文件列表
 * 
 * @param vector 列表
 * @param path   路径
 * @param exts   文件后缀
 */
extern void listFiles(std::vector<std::string>& vector, const std::string& path, const std::vector<std::string>& exts);

/**
 * 遍历文件列表
 * 
 * @param vector    列表
 * @param path      路径
 * @param predicate 路径匹配
 */
template <typename Predicate>
extern void listFiles(std::vector<std::string>& vector, const std::string& path, const Predicate& predicate) {
    if(!std::filesystem::exists(path) || !std::filesystem::is_directory(path)) {
        printf("目录无效：%s", path.data());
        return;
    }
    auto iterator = std::filesystem::directory_iterator(std::filesystem::path(path));
    for(const auto& entry : iterator) {
        std::string filepath = entry.path().string();
        if(entry.is_regular_file()) {
            std::string filename = entry.path().filename().string();
            if(predicate(filename)) {
                vector.push_back(filepath);
            } else {
                printf("忽略无效文件类型：%s", filepath.data());
            }
        } else {
            printf("忽略无效文件：%s", filepath.data());
        }
    }
}

/**
 * @param path 文件路径
 * 
 * @return 文本内容
 */
extern std::string loadFile(const std::string& path);

/**
 * @param path  文件路径
 * @param value 文件内容
 * 
 * @return 是否成功
 */
extern bool saveFile(const std::string& path, const std::string& value);

}
}

#endif // LFR_HEADER_CORE_FILES_HPP
