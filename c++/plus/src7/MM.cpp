#include <map>
#include <string>
#include <iostream>

class T {

public:
    std::string name;

public:
    T() {
        std::cout << "T()\n";
    }
    T(const std::string& name) : name(name) {
    }
    T(const T& t) {
        std::cout << "T(T&)\n";
    }
    T(T&& t) {
        std::cout << "T(T&&)\n";
    }
    T& operator= (const T& t) {
        std::cout << "=(T&)\n";
        return *this;
    }
    T& operator= (T&& t) {
        std::cout << "=(T&&)\n";
        return *this;
    }

};

int main() {
    std::map<std::string, T> map;
    T t;
    map.emplace("1", std::move(t));
    return 0;
}
