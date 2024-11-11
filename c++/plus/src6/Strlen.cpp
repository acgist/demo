#include <string>
#include <sstream>
#include <iostream>

int main() {
    // std::string v = "1234你好";
    // std::cout << v.size()   << '\n';
    // std::cout << v.length() << '\n';
    // std::cout << v.data()   << '\n';
    // std::cout << v.c_str()  << '\n';
    std::stringstream ss;
    size_t v = 4;
    ss.write(reinterpret_cast<char*>(&v), sizeof(v));
    ss.write("1234", 4);
    v = 100;
    ss.write(reinterpret_cast<char*>(&v), sizeof(v));
    ss.write("4321", 4);
    std::stringstream rr(ss.str());
    size_t r;
    std::string s;
    s.resize(4);
    rr.read(reinterpret_cast<char*>(&r), sizeof(r));
    std::cout << r << '\n';
    rr.read(reinterpret_cast<char*>(s.data()), 4);
    std::cout << s << '\n';
    s.clear();
    s.resize(4);
    rr.read(reinterpret_cast<char*>(&r), sizeof(r));
    std::cout << r << '\n';
    rr.read(reinterpret_cast<char*>(s.data()), 4);
    std::cout << s << '\n';
    return 0;
}