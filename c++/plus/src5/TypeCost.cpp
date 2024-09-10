#include <set>
#include <chrono>
#include <string>
#include <iostream>

int main() {
    // std::set<int> seta;
    // seta.insert(1);
    std::set<std::string> seta;
    seta.insert("1");
    auto a = std::chrono::system_clock::now();
    for(int i = 0; i < 200'000'0; ++i) {
        // auto a { seta.find(1) };
        auto a { seta.find("1") };
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << '\n';
    // std::set<int, std::less<>> setb;
    // setb.insert(1);
    std::set<std::string, std::less<>> setb;
    setb.insert("1");
    a = std::chrono::system_clock::now();
    for(int i = 0; i < 200'000'0; ++i) {
        // auto a { setb.find(1) };
        auto a { setb.find("1") };
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << '\n';
    return 0;
}
