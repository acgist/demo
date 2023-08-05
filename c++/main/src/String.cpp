#include <string>
#include <cstring>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::string* v = new std::string("1234");
    std::cout << v << std::endl;
    std::cout << *v << std::endl;
    *v = "12344321";
    std::cout << v << std::endl;
    std::cout << *v << std::endl;
    // std::strcpy();
    char*      cc = "你好";
    wchar_t*   wc = L"你好";
    char16_t* c16 = u"你好";
    char32_t* c32 = U"你好";
    char*     vcc = u8"你好";
    std::cout << cc << std::endl;
    std::cout << *wc << std::endl;
    std::cout << *c16 << std::endl;
    std::cout << *c32 << std::endl;
    std::cout << vcc << std::endl;
    std::string mult = R"(1234""")";
    std::string multEx = R"xx+++*(1234""")")xx+++*";
    std::cout << mult << multEx << std::endl;
    return 0;
}

