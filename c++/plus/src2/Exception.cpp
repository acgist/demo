#include <chrono>
#include <iostream>

int fun() {
    return 0;
}

int funEx() {
    throw 9999;
}

int funExExA() {
    try {
        funEx();
    } catch(const int& e) {
        throw;
    }
    return 0;
}

int funExExB() {
    try {
        funEx();
    } catch(...) {
        throw;
    }
    return 0;
}

int funExExC() {
    funEx();
    return 0;
}

int main() {
    auto a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 100'000; i++) {
        int x = fun();
    }
    auto z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 100'000; i++) {
        try {
            funEx();
        } catch (...) {
        // } catch (const int& e) {
            //
        }
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 100'000; i++) {
        try {
            funExExA();
        } catch (...) {
        // } catch (const int& e) {
            //
        }
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 100'000; i++) {
        try {
            funExExB();
        } catch (...) {
        // } catch (const int& e) {
            //
        }
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    a = std::chrono::system_clock::now();
    for (size_t i = 0; i < 100'000; i++) {
        try {
            funExExC();
        } catch (...) {
        // } catch (const int& e) {
            //
        }
    }
    z = std::chrono::system_clock::now();
    std::cout << std::chrono::duration_cast<std::chrono::microseconds>(z - a).count() << "\n";
    return 0;
}
