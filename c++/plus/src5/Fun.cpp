#include <iostream>
#include <functional>

static int test(int i) {
    return 10 * i;
}

int main() {
    auto                    c{ test };
    std::function           b{ test };
    std::function<int(int)> a{ &test };
    std::cout << a(11) << '\n';
    std::cout << b(22) << '\n';
    std::cout << c(33) << '\n';
    std::plus<int> plus;
    std::cout << plus(1, 2) << '\n';
    return 0;
}