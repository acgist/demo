#include <vector>
#include <string>
#include <random>
#include <iostream>
#include <algorithm>
#include <functional>

int main(int argc, char const *argv[]) {
    std::vector<int> v;
    v.push_back(1);
    v.push_back(1);
    v.push_back(2);
    v.push_back(3);
    v.push_back(4);
    std::cout << std::count(v.begin(), v.end(), 1) << std::endl;
    std::cout << std::count_if(v.begin(), v.end(), [](const int& value) -> bool {
        return value > 1;
    }) << std::endl;
    // std::sort(v.begin(), v.end());
    // std::sort(v.begin(), v.end(), std::greater());
    // std::sort(v.begin(), v.end(), std::greater<int>());
    std::random_device rand;
    std::shuffle(v.begin(), v.end(), std::mt19937(rand()));
    // std::shuffle(v.begin(), v.end(), std::random_device());
    std::for_each(v.begin(), v.end(), [](const int& value) {
        std::cout << value << std::endl;
    });
    std::vector<int> v1;
    std::vector<int> v2;
    std::vector<int> v3;
    v1.push_back(1);
    v1.push_back(2);
    v1.push_back(3);
    v2.push_back(20);
    v2.push_back(1);
    v2.push_back(0);
    v2.push_back(30);
    v3.resize(v1.size() + v2.size());
    // 有序？
    std::merge(v1.begin(), v1.end(), v2.begin(), v2.end(), v3.begin());
    for(std::vector<int>::iterator index = v3.begin(); index != v3.end(); ++index) {
        std::cout << "v3 = " << *index << std::endl;
    }
    std::string x = "12测试34";
    std::cout << x << std::endl;
    std::reverse(x.begin(), x.end());
    std::cout << x << std::endl;
    return 0;
}
