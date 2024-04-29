#include <string>
#include <vector>
#include <iostream>

int main() {
    std::vector<int> vector;
    vector.push_back(1);
    vector.push_back(2);
    vector.push_back(3);
    for(
        auto iterator = vector.end() - 1;
        iterator >= vector.begin();
        iterator--
        // auto iterator = vector.begin();
        // iterator < vector.end();
        // iterator++
    ) {
        std::cout << *iterator << "\n";
    }
    std::string vp = "1234";
    const char* vc = vp.data();
    // const char* vc = vp.c_str();
    return 0;
}