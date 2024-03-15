#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

int main() {
    std::vector<std::string> s = { "1", "2", "3", "456" };
    std::vector<unsigned int> i(s.size());
    // i.resize(s.size());
    // i.reserve(s.size());
    std::transform(s.begin(), s.end(), i.begin(), [](auto& v) -> unsigned int {
        return (unsigned int) v.length();
    });
    std::for_each(s.begin(), s.end(), [](auto& x) {
        std::cout << x << "\n";
    });
    std::cout << "====" << "\n";
    std::for_each(i.begin(), i.end(), [](auto& x) {
        std::cout << x << "\n";
    });
    return 0;
}
