#include <iostream>

bool a() {
    std::cout << "a ";
    return true;
}

bool b() {
    std::cout << "b ";
    return false;
}

int main() {
    if(a() & b()) {
    }
    std::cout << '\n';
    if(b() & a()) {
    }
    std::cout << '\n';
    if(a() && b()) {
    }
    std::cout << '\n';
    if(b() && a()) {
    }
    std::cout << '\n';
    if(a() | b()) {
    }
    std::cout << '\n';
    if(b() | a()) {
    }
    std::cout << '\n';
    if(a() || b()) {
    }
    std::cout << '\n';
    if(b() || a()) {
    }
    std::cout << '\n';
    return 0;
}