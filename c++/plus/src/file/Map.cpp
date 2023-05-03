#include <map>
#include <string>
#include <iostream>
#include <algorithm>

int main(int argc, char const *argv[]) {
    std::map<std::string, int> map{
        { "3", 3 },
        { "0", 0 }
    };
    // std::cout << &map["1234"] << std::endl;
    // std::cout << &map["0"] << std::endl;
    // std::cout << map["1234"] << std::endl;
    // std::cout << map["3"] << std::endl;
    // std::cout << map.count("3") << std::endl;
    // std::cout << map.count("1234") << std::endl;
    // std::cout << map.at("1234") << std::endl;
    // std::cout << &map.find("1234") << std::endl;
    // map.insert("1", 1);
    map["1"] = 1;
    map.insert(std::make_pair("2", 2));
    std::cout << map.at("1") << std::endl;
    std::cout << map["1"] << " = " << map["2"]<< " = " << map["3"] << std::endl;
    std::map<std::string, int>::iterator iterator;
    for (iterator = map.begin(); iterator != map.end(); iterator++) {
        std::cout << &iterator->first << " - " << iterator->first << " = " << iterator->second << std::endl;
    }
    // std::map<std::string, int>::iterator find = map.find("1");
    // std::cout << find->first << std::endl;
    std::for_each(map.begin(), map.end(), [](decltype(*map.begin())& entry) {
        std::cout << &entry.first << " - " << entry.first << " = " << entry.second << std::endl;
    });
    std::for_each(map.begin(), map.end(), [](std::pair<std::string, int> entry) {
        std::cout << &entry.first << " - " << entry.first << " = " << entry.second << std::endl;
    });
    std::for_each(map.begin(), map.end(), [](const std::pair<std::string, int>& entry) {
        std::cout << &entry.first << " - " << entry.first << " = " << entry.second << std::endl;
    });
    // std::for_each(map.begin(), map.end(), [&](std::pair<std::string, int> entry) {
    //     std::cout << &entry.first << " - " << entry.first << " = " << entry.second << std::endl;
    // });
    // std::for_each(map.begin(), map.end(), [=](std::pair<std::string, int> entry) {
    //     std::cout << &entry.first << " - " << entry.first << " = " << entry.second << std::endl;
    // });
    return 0;
}
