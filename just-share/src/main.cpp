#include <iostream>

int main(int argc, char const *argv[]) {
    #if _WIN32
    std::system("chcp 65001");
    #endif
    return 0;
}
