#include <vector>
#include <iterator>
#include <iostream>
#include <algorithm>

int main() {
    std::vector<int> v;
    v.reserve(2);
    std::generate(v.begin(), v.end(), [a = 1]() mutable {
        return ++a;
    });
    std::copy(v.begin(), v.end(), std::ostream_iterator<int>(std::cout, " "));
    v.resize(2);
    std::generate(v.begin(), v.end(), [a = 1]() mutable {
        return ++a;
    });
    std::copy(v.begin(), v.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << '\n';
    std::transform(v.begin(), v.end(), v.begin(), [](const auto& v) { return v * 2; });
    std::copy(v.begin(), v.end(), std::ostream_iterator<int>(std::cout, " "));
    return 0;
}