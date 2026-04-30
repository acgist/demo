#include <memory>
#include <iostream>

// void ptr(std::unique_ptr<int> a) {
//     std::cout << "a\n";
// }

void ptr(std::unique_ptr<int>& a) {
    std::cout << "a&\n";
}

int main() {
    std::unique_ptr<int> a(new int(1));
    ptr(a);
    return 0;
}