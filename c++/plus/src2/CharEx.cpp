#include <string>
#include <iostream>

int main() {
    const char16_t* e = u"a";
    const char32_t* c = U"测试啊1234aass";
    std::cout << *e << "\n";
    std::cout << *c << "\n";
    std::u16string ee = u"1234从";
    std::u32string cc = U"1234从";
    std::cout << ee.data() << "\n";
    std::cout << cc.data() << "\n";
    // std::cout.put(66.6);
    long long x = 12332132131;
    std::cout << x << "\n";
    // std::cout << (char*) &x << "\n";
    std::cout.write((char*) &x, sizeof(x));
    std::cout << "1234" << std::flush;
    std::cout << 1.0/9.0 << "\n";
    std::cout.fill('#');
    std::cout.width(12);
    std::cout << 12 << "*";
    std::cout.width(12);
    std::cout << 12 << "*";
    std::cout.width(2);
    std::cout << 123456 << "\n";
    std::string v = "";
    while(std::cin >> v) {
        std::cout << v << std::endl;
    }
    std::cout.setf(std::ios_base::boolalpha);
    std::cout << std::cin.eof() << std::endl;
    std::cout << std::cin.eofbit << std::endl;
    std::cout << std::cin.badbit << std::endl;
    std::cout << std::cin.failbit;
    std::cin.clear();
    // std::cin.setstate();
    // std::cin.clear(std::ios_base::eofbit);
    // std::cin.sync();
    // std::cin.ignore();
    while(std::cin >> v) {
        std::cout << v << std::endl;
    }
    return 0;
}