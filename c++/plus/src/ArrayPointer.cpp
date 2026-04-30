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

    int a[] = { 1, 2, 3, 4, 5 };
    int count = 0;
    for(int i = 0; i < 5; i++) {
        std::cout << i[a] << a[i] << std::endl;
        if(i[a] % 2 == 0) {
        // if(a[i] % 2 == 0) {
            count++;
        }
    }
    std::cout << "count = " << count << std::endl;
}
