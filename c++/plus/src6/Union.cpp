#include <iostream>

union X {
    short * c;
    int   * i;
};

int main() {
    X x;
    int array[] { 1, 2, 3, 4 };
    x.i = array;
    std::cout << x.i[0] << " = " << x.i[1] << " = " << x.i[2] << " = " << x.i[3] << '\n';
    std::cout << x.c[0] << " = " << x.c[1] << " = " << x.c[2] << " = " << x.c[3] << '\n';
    return 0;
}