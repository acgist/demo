#include <vector>
#include <iostream>
#include <algorithm>

void print(int& value) {
    std::cout << value << " ";
};

class Print {
public:
    // void operator ()(int& value) {
    void operator ()(const int& value) const {
        std::cout << value << " ";
    }
};

int main(int argc, char const *argv[]) {
    std::vector<int> vector;
    vector.push_back(1);
    vector.push_back(2);
    vector.push_back(4);
    vector.push_back(3);
    std::for_each(vector.begin(), vector.end(), print);
    std::cout << std::endl;
    std::for_each(vector.begin(), vector.end(), Print());
    std::cout << std::endl;
    std::vector<int> copy;
    // 设置容量
    copy.resize(vector.size());
    std::transform(vector.begin(), vector.end(), copy.begin(), [](int& value) -> int {
        return value * 10;
    });
    std::for_each(vector.begin(), vector.end(), [](int& value) {
        std::cout << value << " ";
        value = 1;
    });
    std::cout << std::endl;
    std::for_each(vector.begin(), vector.end(), [](const int& value) {
        std::cout << value << " ";
    });
    std::cout << std::endl;
    std::for_each(copy.begin(), copy.end(), [](int& value) {
        std::cout << value << " ";
    });
    std::cout << std::endl;
    return 0;
}
