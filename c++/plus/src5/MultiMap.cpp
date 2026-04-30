#include <map>
#include <iostream>

int main() {
    std::multimap<int, int> map{};
    map.insert(std::make_pair(1, 1));
    map.insert(std::make_pair(1, 2));
    map.emplace(1, 1);
    map.emplace(3, 3);
    map.emplace(1, 3);
    map.emplace(1, 2);
    auto a = map.lower_bound(1);
    auto z = map.upper_bound(1);
    for(; a != z; ++a) {
        std::cout << a->first << " = " << a->second << '\n';
    }
    for(auto& v : map) {
        std::cout << v.first << " = " << v.second << '\n';
    }
    return 0;
}