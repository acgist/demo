#include <string>
#include <vector>
#include <iostream>

int main() {
    std::vector<int> inta = { 1, 2 };
    std::vector<int> intb = { 1, 2 };
    std::vector<int> intc = { 2, 1 };
    std::vector<int> intd = { 1, 2, 3 };
    std::cout << " a == b > " << (inta == intb) << "\n";
    std::cout << " a == c > " << (inta == intc) << "\n";
    std::cout << " a == d > " << (inta == intd) << "\n";
    std::vector<std::string> sa = { "a", "b" };
    std::vector<std::string> sb = { "a", "b" };
    std::vector<std::string> sc = { "b", "a" };
    std::vector<std::string> sd = { "a", "b", "c" };
    std::cout << " a == b > " << (sa == sb) << "\n";
    std::cout << " a == c > " << (sa == sc) << "\n";
    std::cout << " a == d > " << (sa == sd) << "\n";
    return 0;
}