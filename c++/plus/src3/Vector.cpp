#include <vector>
#include <iostream>

int main() {
    std::vector<int> v = {1, 2, 3};
    int array[10]{0};
    array[2] = 1;
    v.erase(v.begin()  + 2);
    v.insert(v.begin() + 2, array, array + 10);
    // std::cout << v << '\n';
    for(auto& x : v) {
        std::cout << x << '\n';
    }
    return 0;
}