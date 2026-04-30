#include <memory>
#include <iostream>

void ptr(std::shared_ptr<int> sharedPtr) {
    std::cout << "ptr -> " << sharedPtr.use_count() << "\n";
}

std::shared_ptr<int> share() {
    int* intPtr = new int{10};
    std::shared_ptr<int> sharedPtr(intPtr);
    return sharedPtr;
}

int main() {
    // std::unique_ptr<int>;
    std::shared_ptr<int> sharedPtr = share();
    std::cout << "ptr -> " << sharedPtr.use_count() << "\n";
    ptr(sharedPtr);
    std::cout << "ptr -> " << sharedPtr.use_count() << "\n";
    return 0;
}