#include <iostream>
#include <stdexcept>

class Exception {

};

void ex() {
    throw Exception{};
}

int main() {
    std::set_terminate([]() {
        std::cout << "t\n";
        std::exit(-1);
    });
    try {
        ex();
    // } catch(const Exception& e) {
    //     std::cerr << "1 = " << '\n';
    } catch(const std::runtime_error& e) {
        std::cerr << "2 = " << e.what() << '\n';
    } catch(const std::exception& e) {
        std::cerr << "3 = " << e.what() << '\n';
    // } catch(...) {
    //     std::cerr << "4 = " << '\n';
    }
    std::cout << "o\n";
    return 0;
}