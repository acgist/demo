#define _CRTDBG_MAP_ALLOC
#include <cstdlib>
#include <crtdbg.h>
#include <iostream>
#include <memory>

#ifdef _DEBUG
    #define DBG_NEW new (_NORMAL_BLOCK, __FILE__, __LINE__)
#else
    #define DBG_NEW new
#endif

#ifdef DBG_NEW
    #define new DBG_NEW
#endif

int main() {
    _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
    int* pi = new int{0};
    // int* pi = new int{0};
    // delete pi;
    // _CrtDumpMemoryLeaks();
    auto x = std::make_unique<int>(new int{0});
    auto y = std::unique_ptr<int>(new int{0});
    std::cout << "1\n";
    return 0;
}