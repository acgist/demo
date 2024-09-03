#include <map>
#include <iostream>
#include <unordered_map>

int main() {
    // std::map<int, int> map;
    std::unordered_map<int, int> map;
    map.emplace(1, 1);
    map.emplace(3, 3);
    map.emplace(2, 2);
    // map.emplace(2, 20);
    // map.insert(std::make_pair(2, 40));
    map.insert_or_assign(2, 20);
    // map.emplace(2, 200);
    // map.try_emplace(2, 200);
    auto a = map.erase(22);
    auto b = map.extract(22);
    for(auto& pair : map) {
        std::cout << pair.first << "=" << pair.second << '\n';
    }
    return 0;
}