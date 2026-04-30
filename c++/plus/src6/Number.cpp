#include <numeric>
#include <iostream>
#include <iterator>

int main() {
    int a[10];
    std::iota(a, a + 10, 10);
    for(const auto& v : a) {
        std::cout << v << '\n';
    }
    int b[10];
    std::iota(a, a + 10, 10);
    auto v = std::inner_product(a, a + 10, b, 0);
    std::cout << v << '\n';
    int data[]{ 3, 1, 4, 1, 5, 9, 2, 6 };
    std::exclusive_scan(data, data + 8, std::ostream_iterator<int>(std::cout, " "), 0);
    std::cout << std::endl;
    std::exclusive_scan(data, data + 8, std::ostream_iterator<int>(std::cout, " "), 1);
    std::cout << std::endl;
    std::inclusive_scan(data, data + 8, std::ostream_iterator<int>(std::cout, " "));
    return 0;
}