#include <iostream>

using namespace std;

int main(int argc, char **argv) {
    int a = 10;
    int b = a;
    int &c = a;
    cout << a << endl;
    cout << b << endl;
    cout << c << endl;
    b = 100;
    cout << a << endl;
    cout << b << endl;
    cout << c << endl;
    c = 200;
    cout << a << endl;
    cout << b << endl;
    cout << c << endl;
    int d = 400;
//    &c = d; // 错误
//    int &e; // 错误
//    int &f = 10; // 错误
    const int &g = 10;
//    g = 100; // 不能修改
}
