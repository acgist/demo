#include <vector>
#include <iostream>

int main() {
    std::vector<int> v;
    for(int i = 0; i < 10; ++i) {
        v.push_back(i);
    }
    std::cout << (long long) (v.data()) << " = " << v.size() << std::endl;
    std::cout << (long long) (v.data()) << " = " << v.size() << std::endl;
    v.erase(v.begin());
    std::cout << (long long) (v.data()) << " = " << v.size() << std::endl;
    return 0;
}