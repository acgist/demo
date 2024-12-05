#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

int main() {
    std::vector<std::string> files{
        "1.source.exe",
        "1.target.exe",
        "2.source.exe",
    };
    const std::string source = "source";
    const std::string target = "target";
    for(const auto& file : files) {
        const auto index = file.find_last_of('.');
        if(index == std::string::npos) {
            std::cout << "加载文件没有标记文件：" << file << '\n';
            continue;
        }
        if(index < source.size()) {
            continue;
        }
        const auto label = file.substr(index - source.size(), source.size());
        std::cout << label << '\n';
        if(label != source) {
            continue;
        }
        std::string target_file(file);
        target_file.replace(index - source.size(), source.size(), target);
        const auto iterator = std::find(files.begin(), files.end(), target_file);
        if(iterator == files.end()) {
            std::cout << "加载文件没有标记文件：" << file << '\n';
            continue;
        }
        std::cout << "加载文件：" << file << " = " << target_file << '\n';
    }
    return 0;
}