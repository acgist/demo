#include <map>
#include <iostream>

int main() {
    std::map<int, int> map;
    std::cout << map.size() << '\n';
    map.emplace(1, 1);
    std::cout << map.size() << '\n';
    int v = map[2];
    std::cout << "2 = " << v << '\n';
    std::cout << map.size() << '\n';
    // map.emplace(1, 2);
    // map[1] = 2;
    map.find(1)->second = 2;
    std::cout << map[1] << '\n';
    return 0;
}