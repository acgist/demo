#include <list>
#include <iostream>

int main() {
    std::list<int> list;
    // auto& x = list.emplace_back(1);
    // std::cout << x << '\n';
    // std::cout << &x << '\n';
    std::cout << &list.emplace_back(1) << '\n';
    std::cout << *list.begin() << '\n';
    auto& c = *list.begin();
    std::cout << c << '\n';
    std::cout << &c << '\n';
    return 0;
}