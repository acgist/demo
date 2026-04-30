#include <vector>
#include <iostream>

int main() {
    std::vector<int> v;
    v.push_back(1);
    v.push_back(2);
    v.push_back(3);
    // v.pop_back();
    v.erase(v.begin());
    std::cout << v.size() << '\n';
    std::cout << v[0] << '\n';
    std::cout << v[1] << '\n';
    return 0;
}