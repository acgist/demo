#include <math.h>
#include <cmath>
#include <string>
#include <climits>
#include <iostream>

int aa;

int main(int argc, char const *argv[]) {
    long long numL = LONG_LONG_MAX;
    int       num  = { numL };
    std::cout << num << std::endl;
    // int  num  = numL;
    // int  num  = 10;
    // long numL = num;
    bool ba = true;
    bool bb = false;
    std::cout << (ba + bb + ba) << std::endl;
    std::cout.setf(std::ios::floatfield);
    // std::cout.setf(std::ios::fixed);
    // const wchar_t x = L'你';
    // std::wcout << x << std::endl;
    // std::wcout << L"你好吗" << std::endl;
    // std::cout << "测试" << std::endl;
    // char16_t vu = u8'你';
    char16_t vv = u'你';
    char32_t vx = U'你';
    std::cout << vv << " = " << vx << std::endl;
    char16_t* vvs = u"南方大厦";
    char32_t* vxs = U"南方大厦";
    std::cout << *vvs << " = " << *vxs << std::endl;
    int age(0);
    // int age{1};
    std::string pIn;
    std::cin >> pIn;
    std::cout << pIn << std::endl;
    int xx = 017;
    char m = 'M';
    std::cout.put(m) << std::endl;
    std::cout.put(76) << std::endl;
    char* pName = "1234";
    std::cout << pName << std::endl;
    std::cout << (int) m << std::endl;
    std::cout << (char) 76 << std::endl;
    std::cout << std::oct << xx << std::endl;
    std::cout << aa << std::endl;
    std::cout << age << std::endl;
    // std::cin >> age;
    // std::string name;
    // std::cin >> name;
    // std::cout << typeid(age).name() << std::endl;
    // std::cout << typeid(name).name() << std::endl;
    std::string a = "a";
    std::string b = "b";
    std::string c = a + b;
    std::cout << c << std::endl;
    const int d = 100;
    std::cout << d << std::endl;
    // final int e = 100;
    // d = 120;
    // INT_MAX;
    std::cout << d << std::endl;
    std::cout << sqrt(6.25) << std::endl;
    std::cout << std::sqrt(6.25) << std::endl;
    return 0;
}
