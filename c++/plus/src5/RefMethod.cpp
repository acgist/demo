#include <iostream>
#include <functional>

int main() {
    auto method{ [a = 0] (const auto& v) mutable {
        a++;
        std::cout << "a = " << a << '\n';
        return true;
    } };
    std::vector<int> vector{ 1, 2, 3 };
    // auto i = std::find_if(vector.begin(), vector.end(), method);
    // auto mref = std::ref(method);
    // auto i = std::find_if(vector.begin(), end, mref);
    // auto i = std::find_if(vector.begin(), vector.end(), method);
    auto i = std::find_if(vector.begin(), vector.end(), std::ref(method));
    if(i == vector.end()) {
    }
    // mref(1);
    // mref(1);
    method(1);
    method(1);
    return 0;
}