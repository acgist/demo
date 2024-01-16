#include <iostream>

#define mul(a, b, c) (a) * (b) * (c);

int main(int argc, char const * argv[]) {
    const int result = mul(1, 2 + 2, 3);
    std::cout << result << std::endl;
    return 0;
}
