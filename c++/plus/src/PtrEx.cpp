#include <memory>
#include <iostream>

int main() {
    std::shared_ptr<int> SPtr1(new int(1));
    std::shared_ptr<int> SPtr2(nullptr);
    std::cout << (SPtr1 == nullptr);
    std::cout << (SPtr2 == nullptr);
    return 0;
}