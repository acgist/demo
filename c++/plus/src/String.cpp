#include <string>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::string ea = "hh";
    std::string eb = "hh";
    std::string ec = "aa";
    std::cout << (ea == eb) << std::endl;
    std::cout << (ea >= eb) << std::endl;
    std::cout << (ea <= eb) << std::endl;
    std::cout << (ea != eb) << std::endl;
    std::cout << (ea == ec) << std::endl;
    std::cout << (ea >= ec) << std::endl;
    std::cout << (ea <= ec) << std::endl;
    std::cout << (ea != ec) << std::endl;
    std::cout << ea.compare(eb) << std::endl;
    std::cout << "====" << std::endl;
    std::string a = "a";
    std::cout << a << std::endl;
    std::string b = a + "b";
    std::cout << b << std::endl;
    std::string c;
    c.assign("c");
    std::cout << c << std::endl;
    c.assign("dddddd", 4);
    std::cout << c << std::endl;
    c.assign(10, 'e');
    std::cout << c << std::endl;
    c.assign("1234567890", 1, 9);
    std::cout << c << std::endl;
    c.assign(1, 2);
    std::cout << c << std::endl;
    std::string cn = "wo测试你好的dd";
    std::cout << cn.find("测试") << std::endl;
    std::cout << cn.find("dd") << std::endl;
    std::cout << cn.find("的d") << std::endl;
    std::cout << cn.size() << std::endl;
    return 0;
}
