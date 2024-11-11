#include <vector>
#include <iostream>

template <
    typename X,
    template <typename E, typename Allocator = std::allocator<E>> typename V = std::vector
>
class T {

public:
    V<X> v;
    // V<std::optional<X>> v;

};

int main() {
    T<int, std::vector> x;
    x.v.push_back(1);
    for(const auto& i : x.v) {
        std::cout << i << '\n';
    }
    return 0;
}
