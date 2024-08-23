#include <string>
#include <iostream>
#include <iterator>

int main() {
    std::cout << "====1\n";
    std::istream_iterator<std::string> begin{ std::cin };
    std::cout << "====2\n";
    std::istream_iterator<std::string> end;
    std::cout << "====3\n";
    for(; begin != end; ++begin) {
        std::cout << *begin << '\n';
    }
    std::cout << "====4\n";
    return 0;
}