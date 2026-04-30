#include <vector>
#include <iterator>
#include <iostream>
#include <algorithm>

int main() {
    std::vector<int> v { 1, 2, 3, 4, 5 };
    std::vector<int> t {};
    t.resize(v.size());
    auto pos = std::copy_if(v.begin(), v.end(), t.begin(), [](const auto& x) { return x > 2; });
    std::cout << v.size() << '\n';
    std::cout << t.size() << '\n';
    std::copy(t.begin(), t.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << '\n';
    t.erase(pos, t.end());
    std::copy(t.begin(), t.end(), std::ostream_iterator<int>(std::cout, " "));
    return 0;
}