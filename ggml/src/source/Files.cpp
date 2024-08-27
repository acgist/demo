#include "../header/Files.hpp"

#include <fstream>
#include <algorithm>

void lifuren::files::listFiles(std::vector<std::string>& vector, const std::string& path) {
    listFiles(vector, path, {});
}

void lifuren::files::listFiles(std::vector<std::string>& vector, const std::string& path, const std::vector<std::string>& exts) {
    listFiles(vector, path, [&](const std::string& filename) -> bool {
        if(exts.empty()) {
            return true;
        } else {
            const size_t pos = filename.find_last_of('.');
            if(pos == std::string::npos) {
                return false;
            }
            std::string ext = filename.substr(pos);
            std::transform(ext.begin(), ext.end(), ext.begin(), ::tolower);
            const auto ret = std::find(exts.begin(), exts.end(), ext);
            return ret != exts.end();
        }
    });
}

std::string lifuren::files::loadFile(const std::string& path) {
    std::ifstream input;
    input.open(path, std::ios_base::in);
    if(!input.is_open()) {
        printf("打开文件失败：%s", path.data());
        input.close();
        return "";
    }
    std::string line;
    std::string lines;
    while(std::getline(input, line)) {
        lines += line;
    }
    input.close();
    return lines;
}

bool lifuren::files::saveFile(const std::string& path, const std::string& value) {
    std::ofstream output;
    output.open(path, std::ios_base::out | std::ios_base::trunc);
    if(!output.is_open()) {
        printf("打开文件失败：%s", path.data());
        output.close();
        return false;
    }
    output << value;
    output.close();
    return true;
}
