#include <set>
#include <list>
#include <string>
#include <chrono>
#include <vector>
#include <iostream>
#include <algorithm>
#include <functional>

int main() {
    auto a = std::chrono::system_clock::now();
    for(int i = 0; i < 100'000; ++i) {
        std::chrono::system_clock::now();
    }
    auto z = std::chrono::system_clock::now();
    auto diff = z - a;
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(diff).count() << "\n";
    std::vector<int> v;
    std::cout << (v.begin() == v.end()) << "\n";
    v.push_back(1);
    std::cout << (v.begin() == v.end()) << "\n";
    std::cout << (v.begin() + 1 == v.end()) << "\n";
    std::cout << (v.begin() + 2 == v.end()) << "\n";
    std::set<int> set;
    set.emplace(5);
    set.emplace(1);
    set.emplace(2);
    set.emplace(4);
    auto li = set.lower_bound(2);
    std::cout << "====\n";
    // for(; li != set.end(); ++li) {
    //     std::cout << *li << "\n";
    // }
    auto ui = set.upper_bound(4);
    std::cout << "====\n";
    // for(; ui != set.end(); --ui) {
    //     std::cout << *ui << "\n";
    // }
    for(; li != ui; ++li) {
        std::cout << *li << "\n";
    }
    std::cout << "====\n";
    std::vector<int> copy;
    copy.resize(4);
    // copy.reserve(4);
    std::transform(set.begin(), set.end(), copy.begin(), [](auto& v) {
        return v + 100;
    });
    std::cout << copy.size() << "\n";
    for(auto& x : copy) {
        std::cout << x << "\n";
    }
    std::cout << "====\n";
    std::list<int> list;
    list.push_back(1);
    list.push_back(2);
    list.push_back(3);
    list.push_back(1);
    // list.remove(1);
    // std::cout << list.size() << "\n";
    auto r = std::remove(list.begin(), list.end(), 1);
    list.erase(r, list.end());
    for(auto& x : list) {
        std::cout << x << "\n";
    }
    std::cout << "size = " << list.size() << "\n";
    std::string str = "1234";
    for(auto& v : str) {
        std::cout << v << "\n";
    }
    std::cout << str << "\n";
    std::vector<bool> vv { true, true, false, true };
    // 不能使用：auto&
    // 可以使用：auto&&
    for(const auto& x : vv) {
        std::cout << x << "\n";
    }
    return 0;
}