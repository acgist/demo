#include <iostream>

using namespace std;

#define E 10;

// 全局变量：全局区
int c = 30;
const int f = 60;
const static int g = 70;

int main(int argc, char **argv) {
    // 局部变量：
    int a = 10;
    int b = 20;
    cout << &a << endl;
    cout << &b << endl;
    cout << &c << endl;

    // 静态变量：全局区
    static int d = 40;
    cout << &d << endl;

    // 字符串常量
    cout << &"acgist" << endl;
//    cout << &E << endl;

    // const
    const int e = 50;
    cout << &e << endl;

    // const static
    const static int h = 80;
    cout << &f << endl;
    cout << &h << endl;

//    cout << &(10) << endl;
}
