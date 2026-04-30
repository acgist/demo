#include <iostream>
#include <exception>

using namespace std;

int main() {
    int a = 100;
    int b = 10;
    int c = a / b;
    cout << c << endl;
    b = 0;
    try {
        if (b == 0) {
            throw "除数不能为零";
        }
        // 除零是不能捕获的
        c = a / b;
    } catch (char const *e) {
        cout << "异常：" << e << endl;
    } catch (exception &e) {
        cout << "异常：" << e.what() << endl;
    }
    cout << c << endl;
    return 0;
}
