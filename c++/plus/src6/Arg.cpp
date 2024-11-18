#include <iostream>

void print() {
    std::cout << "end\n";
}

template<typename T1, typename ... Tn>
void print(T1&& t1, Tn&&... tn) {
    std::cout << t1 << " = " << sizeof...(tn) << '\n';
    // print(tn...);
    print(std::forward<Tn>(tn)...);
}

template<typename T1, typename ... Tn>
T1 plus(T1&& t1, Tn&&... tn) {
    return (t1 + ... + tn);
}

int main() {
    print(1, "2", 3);
    // print(1, 2, 3);
    // std::cout << plus() << '\n';
    std::cout << plus(1) << '\n';
    std::cout << plus(1, 2) << '\n';
    std::cout << plus(1, 2, 3) << '\n';
    return 0;
}