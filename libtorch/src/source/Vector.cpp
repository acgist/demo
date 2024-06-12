#include <vector>
#include <iostream>

int main() {
    std::vector<int> a{10, 0};
    std::cout << a.size() << "\n";
    std::vector<int> b(10);
    std::cout << b.size() << "\n";
    return 0;
}