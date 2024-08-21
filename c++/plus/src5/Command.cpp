#include <array>
#include <cstdio>
#include <string>
#include <iostream>

#if _WIN32
#define popen  _popen
#define pclose _pclose
#endif

int main() {
    std::string result;
    char chars[16];
    // r w r+ w+ rt+ wt+ rb+ wb+
    // FILE* pipe = popen("dir", "r");
    FILE* pipe = popen("ping www.acgist.com", "r");
    // FILE* pipe = popen("tasklist", "r");
    // FILE* pipe = popen("netstat -ano", "r");
    if(!pipe) {
        return -1;
    }
    while(fgets(chars, 16, pipe) != nullptr) {
        result += chars;
        std::cout << "========================================" << std::endl;
        // pclose(pipe);
    }
    int code = pclose(pipe);
    std::cout << "========================================" << std::endl;
    std::cout << code << '\n';
    std::cout << "========================================" << std::endl;
    std::cout << result << '\n';
    return 0;
}