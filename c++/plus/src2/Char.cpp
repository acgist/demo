#include <stdint.h>
#include <sstream>
#include <iostream>

int main() {
    char* surfaceId = new char[sizeof(char) + 1] { 0 };
    std::cout << sizeof(char) << "\n";
    surfaceId[0] = 48;
    std::cout << surfaceId << "\n";
    uint64_t iSurfaceId;
    std::istringstream iss(surfaceId);
    iss >> iSurfaceId;
    std::cout << iSurfaceId << "\n";
    return 0;
}