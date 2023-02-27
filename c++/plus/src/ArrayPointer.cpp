#include <iostream>

using namespace std;

int main(int argc, char **argv) {

    int array[] = { 1, 2, 3 };
    int *p = array;
    cout << *p << endl;
    p++;
    cout << *p << endl;
    // 注意区分下面两种区别
    cout << *(++p) << endl;
    cout << *(p++) << endl;

}
