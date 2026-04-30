#include <iostream>
#include <exception>

void ex() {
    const char* message = "1234";
    throw std::runtime_error(message);
    // throw "1234";
}

int main() {
    try {
        ex();
    } catch(const std::exception& e) {
        std::cerr << e.what() << " 1\n";
    } catch(const char* e) {
        std::cerr << e << " 2\n";
    } catch(...) {
        std::cerr << "====\n";
    }
    return 0;
}