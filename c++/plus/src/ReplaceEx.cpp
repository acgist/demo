#include <regex>
#include <string>
#include <iostream>

int main() {
    std::string v = "1,2,3,4";
    std::cout << v << "\n";
    // std::string x = std::regex_replace(v, std::regex(","), "+");
    // std::string x = std::regex_replace(v, std::regex("\\d"), "+");
    // std::string x = v.replace(v.begin(), v.end(), ",", "-");
    size_t index = 0;
    while(true) {
        index = v.find(",", index);
        if(index == std::string::npos) {
            break;
        }
        v.replace(index, 1, "+");
        ++index;
    }
    std::cout << v << "\n";
    // std::cout << x << "\n";
    return 0;
}