#include <map>
#include <string>
#include <iostream>

int main() {
    std::string* xPtr = new std::string("1234");
    // std::string x = "1234";
    // const char* xc = x.c_str();
    // char* xd = x.data();
    // std::cout << x  << std::endl;
    // std::cout << xc << std::endl;
    // std::cout << xd << std::endl;
    // xd[0] = '2';
    // std::cout << x  << std::endl;
    // std::cout << xc << std::endl;
    // std::cout << xd  << std::endl;
    std::map<int, std::string> map;
    map.insert(std::make_pair(1, *xPtr));
    map.insert(std::make_pair(2, std::move(*xPtr)));
    std::string& xRef = map.at(1);
    std::string& xRefEx = map.at(2);
    std::cout << *xPtr << std::endl;
    std::cout << xRef << std::endl;
    std::cout << xRefEx << std::endl;
    xRef = "123456";
    xRefEx = "123456";
    std::cout << *xPtr << std::endl;
    std::cout << xRef << std::endl;
    std::cout << xRefEx << std::endl;
    delete xPtr;
    std::cout << "finish" << std::endl;
    return 0;
}