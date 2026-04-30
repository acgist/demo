#include <memory>
#include <iostream>

std::unique_ptr<int> get() {
    std::unique_ptr<int> p(new int(111));
    // return std::move(p);
    return p;
}

int main() {
    std::unique_ptr<int> intPtr(new int(100));
    std::unique_ptr<int> copyPtr = nullptr;
    copyPtr = std::move(intPtr);
    std::cout << (intPtr == nullptr)  << "\n";
    std::cout << *copyPtr << "\n";
    // std::shared_ptr<int> intPtr(new int(100));
    // std::shared_ptr<int> copyPtr = nullptr;
    // copyPtr = intPtr;
    // std::cout << *intPtr  << "\n";
    // std::cout << *copyPtr << "\n";
    std::unique_ptr<int> pp = get();
    std::cout << *pp << "\n";
}
