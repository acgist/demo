#include <string>
#include <sstream>
#include <cstdint>
#include <iostream>

int main() {
    // // int a = 100;
    // // char chars[4];
    // // chars = (char*) &a;
    // long long count = 100000000L;
    // // char num[8];
    // char* num;
    // num = (char*) (&count);
    // // char* numptr = num;
    // // numptr = (char*) (&count);
    // std::cout << (int) num[0] << "\n";
    // std::cout << (int) num[1] << "\n";
    // std::cout << (int) num[2] << "\n";
    // std::cout << (int) num[3] << "\n";
    // std::cout << (int) num[4] << "\n";
    // std::cout << (int) num[5] << "\n";
    // std::cout << (int) num[6] << "\n";
    // std::cout << (int) num[7] << " ---- \n";
    // char* x = (char*) (&count);
    // std::cout << (int) x[0] << "\n";
    // std::cout << (int) x[1] << "\n";
    // std::cout << (int) x[2] << "\n";
    // std::cout << (int) x[3] << "\n";
    // std::cout << (int) x[4] << "\n";
    // std::cout << (int) x[5] << "\n";
    // std::cout << (int) x[6] << "\n";
    // std::cout << (int) x[7] << "\n";
    uint64_t pid = 1234;
    char* surfaceIdx = (char*) (&pid);
    char surfaceId[9]{0};
    memcpy(surfaceId, surfaceIdx, 8);
    // char* surfaceId = std::to_string(pid).data();
    uint64_t iSurfaceId;
    std::istringstream iss(surfaceId);
    iss >> iSurfaceId;
    std::cout << iSurfaceId;
    return 0;
}