#include <iostream>
#include <functional>

int get(int v) {
    return v;
}

template<typename T>
T tmp(T v) {
    return v;
}

int main() {
    using ir1 = std::invoke_result<decltype(get), int>::type;
    using ir2 = std::invoke_result<decltype(&get), int>::type;
    using dt1 = decltype(get(1));
    const auto& ir1t = typeid(ir1);
    const auto& ir2t = typeid(ir2);
    const auto& dt1t = typeid(dt1);
    std::cout << ir1t.name() << '\n';
    std::cout << ir2t.name() << '\n';
    std::cout << dt1t.name() << '\n';
    using ir3 = std::invoke_result<decltype(&tmp<int>), int>::type;
    using dt2 = decltype(tmp<int>(1));
    const auto& ir3t = typeid(ir3);
    const auto& dt2t = typeid(dt2);
    std::cout << ir3t.name() << '\n';
    std::cout << dt2t.name() << '\n';
    return 0;
}