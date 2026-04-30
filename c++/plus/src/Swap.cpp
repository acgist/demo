#include <iostream>

using namespace std;

void swap(int source, int target) {
    int tmp = source;
    source = target;
    target = tmp;
}

void swapOld(int *source, int *target) {
//    int tmp = *source;
//    *source = *target;
//    *target = tmp;
    int *tmp = source;
    source = target;
    target = tmp;
}

int main(int argc, char **argv) {
    int a = 10;
    int b = 20;
    cout << "a = " << a << " b = " << b << endl;
    swap(a, b);
    cout << "a = " << a << " b = " << b << endl;
    swapOld(&a, &b);
    cout << "a = " << a << " b = " << b << endl;
}
