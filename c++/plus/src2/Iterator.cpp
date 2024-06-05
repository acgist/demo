#include <vector>
#include <iostream>
#include <iterator>

int main() {
    std::vector<int> vector;
    vector.push_back(1);
    vector.push_back(2);
    vector.push_back(3);
    vector.push_back(4);
    // auto i = vector.begin();
    // std::cout << *i << "\n";
    // std::cout << &(*i) << "\n";
    // std::ostream_iterator<int> oi(std::cout, " ");
    // *oi = 123;
    for(auto i = vector.begin(); i != vector.end(); ++i) {
        std::cout << *i << "\n";
    }
    for(auto i = vector.rbegin(); i != vector.rend(); ++i) {
        std::cout << *i << "\n";
    }
    std::cout << " = \n";
    for(auto i = vector.begin(); i != vector.end(); ++i) {
        std::cout << *i << "\n";
        vector.erase(i);
        // --i;
    }
    return 0;
}