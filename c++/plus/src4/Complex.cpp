#include <complex>
#include <iomanip>
#include <iostream>

int main() {
    std::cout << std::setprecision(8) << (1.1 + 1.3) << '\n';
    std::cout << std::setprecision(16) << (5726.8673 + 5837.7540) << '\n';
    std::complex<double> a = 1.1;
    std::complex<double> b = 1.1;
    std::cout << (a + b).real() << '\n';
    std::cout << (a + b).imag() << '\n';
    return 0;
}