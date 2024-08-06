#include <string>
#include <sstream>
#include <iostream>

int main() {
    std::string v;
    std::ostringstream out;
    while(std::cin >> v) {
        if(v == "exit") {
            break;
        }
        out << v << '\n';
    }
    std::cout << out.str();
    return 0;
}
