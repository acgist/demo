#include <string>
#include <chrono>
#include <vector>
#include <fstream>
#include <iostream>
#include <algorithm>
#include <filesystem>
#include <unordered_map>

static void loadVectors(const std::string& path) {
    if(path.empty()) {
        return;
    }
    std::ifstream input;
    input.open(path, std::ios::in);
    if(!input.is_open()) {
        input.close();
        return;
    }
    char * beg { nullptr };
    char * pos { nullptr };
    char * lend{ nullptr };
    char * bend{ nullptr };
    size_t dims{ 0 };
    const size_t size = std::filesystem::file_size(std::filesystem::u8path(path));
    std::vector<char> data(size);
    char *buffer = data.data();
    input.read(buffer, size);
    bend = buffer + input.gcount();
    lend = std::find(buffer, bend, '\n');
    pos  = std::find(buffer, lend, ' ');
    dims = std::strtod(pos, &pos);
    if(lend == bend || dims == 0) {
        input.close();
        return;
    }
    pos  = lend + 1;
    beg  = pos;
    std::string word;
    std::vector<float> vector;
    // 使用临时变量接收最后赋值防止重入问题
    std::unordered_map<std::string, std::vector<float>> copy;
    while(true) {
        vector.reserve(dims);
        lend = std::find(beg, bend, '\n');
        pos  = std::find(beg, lend, ' ');
        word = std::string(beg, pos);
        if(word.empty()) {
            break;
        }
        ++pos;
        while(pos < lend) {
            vector.emplace_back(std::strtof(pos, &pos));
            ++pos;
        }
        copy.emplace(word, std::move(vector));
        if(lend < bend) {
            pos = lend + 1;
            beg = pos;
        } else {
            break;
        }
    }
    input.close();
}

int main() {
    auto a = std::chrono::system_clock::now();
    loadVectors("D:/tmp/lifuren/Chinese-Word-Vectors/sgns.sikuquanshu.word");
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(z - a).count();
    return 0;
}