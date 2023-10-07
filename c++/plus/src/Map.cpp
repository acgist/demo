#include <map>
#include <iostream>

class Sort {
public:
    bool operator()(const int& a, const int& z) const {
        return a > z;
    }
};

int main(int argc, char const *argv[]) {
    std::map<int, int, Sort> map;
    map.insert(std::pair<int, int>(1, 1));
    map.insert(std::pair<int, int>(2, 1));
    map.insert(std::pair<int, int>(3, 1));
    map.insert(std::pair<int, int>(4, 1));
    map.insert(std::pair<int, int>(0, 1));
    for(auto i = map.begin(); i != map.end(); i++) {
        std::cout << i->first << i->second << std::endl;
    }
    return 0;
}
