#include <string>
#include <vector>
#include <iostream>

int main() {
    // using namespace 
    std::vector<int> a { 1, 2, 3 };
    std::vector<int> b { 2, 2, 3 };
    std::vector<int> c { 2, 2, 3 };
    std::cout << (a == b) << '\n';
    std::cout << (c == b) << '\n';
    std::vector<std::string> d { "1", "2", "3" };
    std::vector<std::string> e { "2", "2", "3" };
    std::vector<std::string> f { "2", "2", "3" };
    std::cout << (d == e) << '\n';
    std::cout << (f == e) << '\n';
    return 0;
}