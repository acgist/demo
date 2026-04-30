#include <vector>
#include <iostream>
#include <iterator>

template<typename T>
void type(T t) {
    typename std::iterator_traits<T>::value_type v;
    v = *t;
    std::cout << v << '\n';
}

int main() {
    std::vector<int> v;
    std::cout << sizeof(v) << '\n';
    v.push_back(1);
    v.push_back(1);
    v.push_back(1);
    v.push_back(1);
    std::cout << sizeof(v) << '\n';
    type(v.begin());
    return 0;
}