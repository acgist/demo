#include <string>
#include <iostream>

int main( ) {
// int main(int argc, char const *argv[]) {
    std::string line = "1"
    "2\n"
    "3";
    std::cout << line << std::endl;
    line = R"(
    12
    3
    )";
    std::cout << line << std::endl;
    line = "12\n\
3";
    std::cout << line << std::endl;
    return 0;
}
