#include <locale>
#include <string>
#include <iostream>
#include "string.h"

/**
 * Linux执行
 */
int main(int argc, char const *argv[]) {
    // std::locale zhCn("zh_CN.utf8");
    std::ios::sync_with_stdio(false);
    std::locale zhCn("");
    // std::locale zhCn("zh_CN");
    std::wcin.imbue(zhCn);
    std::wcout.imbue(zhCn);
    while(true) {
        // char value[128];
        // std::cout << "请输入内容：";
        // std::cin >> value;
        // std::cout << "输入的内容：" << value << " = " << strlen(value) << std::endl;
        // if(value == "0") {
        //     break;
        // }
        // std::string value;
        // std::cout << "请输入内容：";
        // std::cin >> value;
        // std::cout << "输入的内容：" << value << std::endl;
        // if(value == "0") {
        //     break;
        // }
        std::wstring value;
        std::wcout << L"请输入内容：";
        std::wcin >> value;
        std::wcout << value << std::endl;
        //if(value == L"0") {
       //     break;
      //  }
    }
    return 0;
}
