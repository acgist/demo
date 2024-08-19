#include <map>
#include <iostream>

int main() {
    std::map<int, int> map;
    auto x = map.emplace(1, 3);
    std::cout << x.first->first << '\n';
    std::cout << x.first->second << '\n';
    std::cout << x.second << '\n';
    x = map.emplace(1, 2);
    std::cout << x.first->first << '\n';
    std::cout << x.first->second << '\n';
    std::cout << x.second << '\n';
    x = map.emplace(1, 1);
    std::cout << x.first->first << '\n';
    std::cout << x.first->second << '\n';
    std::cout << x.second << '\n';
    std::cout << map[1] << '\n';
    std::cout << "====\n";
    auto old = map[1] = 100;
    std::cout << old << '\n';
    return 0;
}