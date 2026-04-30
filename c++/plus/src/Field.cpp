#include <iostream>

class Test {

public:
  int a;
  int* b;
  Test();

};

Test::Test() {
    std::cout << "----" << "\n";
    int a = 100;
    this->a = a;
    this->b = &a;
    // this->b = &(this->a);
    // int* a = new int { 100 };
    // this->a = *a;
    // this->b = a;
}

int main() {
    Test test;
    std::cout << test.a << "\n";
    std::cout << test.b << "\n";
    std::cout << *test.b << "\n";
    std::cout << "====\n";
    Test test2 = test;
    std::cout << "====\n";
    Test test3 = Test();
    return 0;
}