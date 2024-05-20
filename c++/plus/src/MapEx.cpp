#include <map>
#include <iostream>

int main() {
    std::map<int, int> map;
    map.insert(std::make_pair(1, 1));
    std::cout << map[1] << std::endl;
    std::cout << map[2] << std::endl;
    std::cout << map.at(1) << std::endl;
    // std::cout << map.at(3) << std::endl;
    std::cout << (map.find(1) == map.end()) << std::endl;
    std::cout << (map.find(4) == map.end()) << std::endl;
    return 0;
}