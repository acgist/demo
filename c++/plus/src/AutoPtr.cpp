#include <memory>
#include <iostream>

class Person {

public:
const char* name = "1234";
~Person() {
    std::cout << "析构" << std::endl;
}

};

std::unique_ptr<Person> getUUPtr() {
    std::unique_ptr<Person> pUPtr(new Person());
    return pUPtr;
}

std::shared_ptr<Person> getUSPtr() {
    std::shared_ptr<Person> pSPtr(new Person());
    return pSPtr;
}

int main() {
    // Person* uPtr = new Person();
    // std::unique_ptr<Person> uUPtr(new Person());
    auto uSPtr     = getUSPtr();
    auto uSPtrCopy = uSPtr;
    std::cout << uSPtr->name << std::endl;
    std::cout << uSPtr->name << std::endl;
    std::cout << uSPtr->name << std::endl;
    std::cout << uSPtr->name << std::endl;
    std::cout << uSPtr.use_count() << std::endl;
    return 0;
}