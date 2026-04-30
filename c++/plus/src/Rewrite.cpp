#include <iostream>

using namespace std;

void fun(int &a) {
    cout << "func int &a" << endl;
}

void fun(const int &a) {
    cout << "func const int &a" << endl;
}

// 可以编译通过：但是两个参数就会出现二义性
//void fun(int &a, int b) {
//    cout << "fun int &a b c default" << endl;
//}

void fun(int &a, int b, int c = 10) {
    cout << "fun int &a b c default" << endl;
}

int main(int argc, char **argv) {
    int a = 10;
    fun(a);
    fun(10);
    const int b = 10;
    fun(b);
    fun(a, b);
}
