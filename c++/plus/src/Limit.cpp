#include <limits>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::cout << std::numeric_limits<int>::min() << std::endl;
    std::cout << std::numeric_limits<int>::max() << std::endl;
    std::cout << std::numeric_limits<long>::min() << std::endl;
    std::cout << std::numeric_limits<long>::max() << std::endl;
    std::cout << std::numeric_limits<long long>::min() << std::endl;
    std::cout << std::numeric_limits<long long>::max() << std::endl;
    std::cout << std::numeric_limits<double>::min() << std::endl;
    std::cout << std::numeric_limits<double>::max() << std::endl;
    std::cout << std::numeric_limits<unsigned int>::min() << std::endl;
    std::cout << std::numeric_limits<unsigned int>::max() << std::endl;
    std::cout << std::numeric_limits<int>::digits << std::endl;
    std::cout << std::numeric_limits<unsigned int>::digits << std::endl;
    std::cout << std::numeric_limits<unsigned int>::infinity() << std::endl;
    wchar_t v[] = L"中文";
    std::wcout << v;
    return 0;
}
