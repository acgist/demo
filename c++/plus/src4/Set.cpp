#include <set>
#include <list>
#include <iostream>

int main() {
    std::list<std::string> set;
    std::string x = "1234";
    // set.insert(x);
    set.push_back(x);
    std::cout << x << '\n';
    // set.emplace(x);
    set.emplace_back(x);
    std::cout << x << '\n';
    return 0;
}