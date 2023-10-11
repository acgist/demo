#include <vector>
#include <iostream>
#include <algorithm>

int main(int argc, char const *argv[]) {
    std::vector<int> source;
    source.push_back(1);
    source.push_back(2);
    source.push_back(3);
    std::vector<int> target;
    target.push_back(1);
    target.push_back(4);
    target.push_back(5);
    std::vector<int> result;
    result.resize(std::min(source.size(), target.size()));
    std::vector<int>::iterator end = std::set_intersection(source.begin(), source.end(), target.begin(), target.end(), result.begin());
    std::cout << "交集：";
    std::for_each(result.begin(), end, [](int& value) {
        std::cout << value << " ";
    });
    std::cout << std::endl;
    result.clear();
    result.resize(source.size() + target.size());
    end = std::set_union(source.begin(), source.end(), target.begin(), target.end(), result.begin());
    std::cout << "并集：";
    std::for_each(result.begin(), end, [](int& value) {
        std::cout << value << " ";
    });
    std::cout << std::endl;
    result.clear();
    result.resize(std::max(source.size(), target.size()));
    end = std::set_difference(source.begin(), source.end(), target.begin(), target.end(), result.begin());
    std::cout << "差集：";
    std::for_each(result.begin(), end, [](int& value) {
        std::cout << value << " ";
    });
    std::cout << std::endl;
    return 0;
}
