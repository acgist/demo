#include <iostream>

using namespace std;

int main(int argc, char **argv) {

    int array[] = { 321, 32, 321, 932, 53, 532, 553 };
    int max = array[0];
    for (int index = 1; index < sizeof(array) / sizeof(max); index++) {
        if(max < array[index]) {
            max = array[index];
        }
    }
    cout << "max = " << max << endl;
    cout << array << endl;
    cout << array[0] << endl;
    cout << array[1] << endl;
    cout << &array[0] << endl;
    cout << &array[1] << endl;

}
