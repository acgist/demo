#include <string>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::string a = "1234";
    std::string b = a;
    std::string& c = a;
    std::string* d = &a;
    std::cout << &a << std::endl;
    std::cout << &b << std::endl;
    std::cout << &c << std::endl;
    std::cout << d << std::endl;
    a = "432100";
    std::cout << a << std::endl;
    std::cout << b << std::endl;
    std::cout << c << std::endl;
    std::cout << *d << std::endl;
    std::cout << &a << std::endl;
    std::cout << &b << std::endl;
    std::cout << &c << std::endl;
    std::cout << d << std::endl;
    a = b + "1122";
    std::cout << a << std::endl;
    std::cout << b << std::endl;
    std::cout << c << std::endl;
    std::cout << *d << std::endl;
    std::cout << &a << std::endl;
    std::cout << &b << std::endl;
    std::cout << &c << std::endl;
    std::cout << d << std::endl;
    int  aa = 10000;
    int  bb = aa;
    int& cc = aa;
    std::cout << std::endl;
    std::cout << &aa << std::endl;
    std::cout << &aa << std::endl;
    std::cout << &cc << std::endl;
    aa = 200;
    std::cout << &aa << std::endl;
    std::cout << &aa << std::endl;
    std::cout << &cc << std::endl;
    std::cout << aa << std::endl;
    std::cout << aa << std::endl;
    std::cout << cc << std::endl;
    std::string sa[10] = { "1", "123456789009876543211234567890098765432112345678900987654321" };
    int         ia[10];
    std::cout << sizeof(sa) / sizeof(sa[0]) << std::endl;
    std::cout << sizeof(ia) / sizeof(ia[0]) << std::endl;
    std::cout << sa[0] << std::endl;
    std::cout << sizeof(sa) << " = = " << std::endl;
    std::cout << sizeof(sa[0]) << " = = " << std::endl;
    std::cout << sizeof(sa[1]) << " = = " << std::endl;
    std::string* v = sa;
    std::cout << *v << std::endl;
    std::cout << *(v + 1) << std::endl;
    std::string *psa = new std::string[10] { "1", "12345678900987654321" };
    int         *pia = new int[10] {0};
    std::cout << "diff = " << (&(pia[9]) - &(pia[0])) << std::endl;
    std::cout << sizeof(psa) / sizeof(psa[0]) << std::endl;
    std::cout << sizeof(pia) / sizeof(pia[0]) << std::endl;
    std::cout << psa[0] << std::endl;
    std::cout << sizeof(psa) << " = = " << std::endl;
    std::cout << sizeof(psa[0]) << " = = " << std::endl;
    std::cout << sizeof(psa[1]) << " = = " << std::endl;
    std::string* pv = psa;
    std::cout << *pv << std::endl;
    std::cout << *(pv + 1) << std::endl;
    delete[] psa;
    delete[] pia;
    int* age = new int(1);
    std::cout << age[0] << std::endl;
    std::cout << *age << std::endl;
    delete age;
    char* chars = "acgist";
    std::cout << (int*) chars << std::endl;
    std::cout << chars[0] << std::endl;
    std::cout << &chars << std::endl;
    int nn[] = { 0, 1, 2, 3, 4 };
    std::cout << nn[0] << std::endl;
    std::cout << nn[1] << std::endl;
    std::cout << nn[-1] << std::endl;
    int* pnn = &(nn[2]);
    std::cout << *pnn << std::endl;
    std::cout << pnn[1] << " = " << *(pnn + 1) << std::endl;
    std::cout << pnn[-1] << " = " << *(pnn - 1) << std::endl;
    return 0;
}
