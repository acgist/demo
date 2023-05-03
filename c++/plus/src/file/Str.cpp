#include <string>
#include <iostream>

int main(int argc, char const *argv[]) {
    // const char* v = "1234";
    const char* v = new char[10]{"1234"};
    std::cout << v << std::endl;
    delete []v;
    std::cout << v << std::endl;
    std::cout << v << std::endl;
    std::string a = "1234";
    std::string b = "1234";
    std::string c = "1233";
    std::cout << (a == b) << std::endl;
    std::cout << (a == c) << std::endl;
    std::cout << a.compare(b) << std::endl;
    std::cout << a.compare(c) << std::endl;
    return 0;
}
