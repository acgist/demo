#include <vector>
#include <iostream>
#include <algorithm>

int main(int argc, char const *argv[]) {
    std::vector<int> v;
    v.push_back(1);
    v.push_back(2);
    v.push_back(4);
    v.push_back(3);
    std::vector<int> copy;
    std::replace(v.begin(), v.end(), 1, 10);
    copy.resize(v.size());
    std::copy(v.begin(), v.end() - 1, copy.begin());
    // std::swap(v, copy);
    std::for_each(v.begin(), v.end(), [](int& value) {
        std::cout << "v = " << value << std::endl;
    });
    std::for_each(copy.begin(), copy.end(), [](int& value) {
        std::cout << "copy = " << value << std::endl;
    });
    return 0;
}
