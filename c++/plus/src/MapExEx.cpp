#include <map>
#include <iostream>

int main() {
    std::map<int, int> map = {
        {1, 1},
        {2, 2},
    };
    std::cout << map.size() << "\n";
    for(auto iterator = map.rbegin(); iterator != map.rend(); ++iterator) {
    // for(auto iterator = map.begin(); iterator != map.end(); ++iterator) {
        // map.erase(iterator);
        std::cout << iterator->first << "\n";
    }
    std::cout << map.size() << "\n";
    return 0;
}