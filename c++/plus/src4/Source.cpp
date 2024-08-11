#include <iostream>
#include <source_location>

int main() {
    std::cout << std::source_location::current().line() << '\n';
    return 0;
}