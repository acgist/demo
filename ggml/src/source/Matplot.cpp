#include "../header/Matplot.hpp"

#include <algorithm>

#include "matplot/matplot.h"

std::map<std::string, size_t> acgist::countCols(const std::vector<std::vector<std::string>>& data, const int& col, const int& pos) {
    if(data.empty()) {
        return {};
    }
    std::map<std::string, size_t> ret;
    std::for_each(data.begin() + pos, data.end(), [&ret, &col](auto& row) {
        auto& value = row.at(col);
        auto entry = ret.find(value);
        if(entry == ret.end()) {
            ret.emplace(value, 1);
        } else {
            ret.emplace(value, entry->second + 1);
        }
    });
    return ret;
}

std::vector<std::string> acgist::selectCols(const std::vector<std::vector<std::string>>& data, const int& col, const int& pos) {
    if(data.empty()) {
        return {};
    }
    std::vector<std::string> ret;
    ret.reserve(data.size());
    std::for_each(data.begin() + pos, data.end(), [&ret, &col](auto& row) {
        ret.push_back(row.at(col));
    });
    return ret;
}

void acgist::plotPie(const std::map<std::string, size_t>& map) {
    if(map.empty()) {
        return;
    }
    std::vector<double> data;
    data.reserve(map.size());
    std::vector<std::string> labels;
    labels.reserve(map.size());
    auto plot = matplot::figure();
    plot->name("vector");
    plot->number_title(false);
    matplot::hold(matplot::on);
    std::for_each(map.begin(), map.end(), [&data, &labels](auto& v) {
        data.push_back(v.second);
        labels.push_back(v.first);
    });
    matplot::pie(data, labels);
    matplot::show(plot);
}

#include <iostream>

void acgist::plotHist(const std::vector<std::string>& vector) {
    if(vector.empty()) {
        return;
    }
    auto plot = matplot::figure();
    plot->name("vector");
    plot->number_title(false);
    matplot::hold(matplot::on);
    std::vector<double> ret;
    ret.resize(vector.size());
    std::transform(vector.begin(), vector.end(), ret.begin(), [](auto& v) {
        return std::atof(v.c_str());
    });
    matplot::hist(ret);
    matplot::show(plot);
}