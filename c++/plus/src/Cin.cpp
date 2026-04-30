#include <iostream>

int main(int argc, char const *argv[]) {
    int num = -1;
    bool success = false;
    if(std::cin >> num) {
        success = true;
    }
    std::cout << num << " = " << success << std::cin.good() << std::endl;
    return 0;
}
