#include <iostream>

int main() {
    // char* v = "1234";
    const char* v = "1234";
    std::cout << v << "\n";
    // delete[] v;
    char* vPtr = new char[1024];
    memcpy(vPtr, "1234", 4);
    std::cout << vPtr << "\n";
    delete[] vPtr;
    return 0;
}