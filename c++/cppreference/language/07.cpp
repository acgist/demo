#include <cstdio>
#include <vector>
#include <numeric>
#include <iostream>

int main() {
    std::vector<char> v;
    v.resize(1024, '1');
    v[4] = '\0';
    v[6] = '\0';
    v[7] = '\0';
    v[8] = '\0';
    v[9] = '\0';
    v.resize(20);
    for(auto iter = v.begin(); iter != v.end(); ++iter) {
        std::cout << *iter << std::endl;
    }
    return 0;
}
