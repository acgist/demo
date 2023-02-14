#include <iostream>

using namespace std;

int main(int argc, char **argv) {

//  int array[] = { 5, 2, 3, 4, 6 };
    int array[] = { 1, 34, 43, 21, 4, 534, 99, 432, 37, 43, 9, 3 };
    const int length = sizeof(array) / sizeof(array[0]);
    for (int index = 0; index < length; ++index) {
        bool change = false;
        for (int jndex = 0; jndex < length - index - 1; ++jndex) {
            if(array[jndex] > array[jndex + 1]) {
                int tmp = array[jndex];
                array[jndex] = array[jndex + 1];
                array[jndex + 1] = tmp;
                change = true;
            }
        }
        if(!change) {
            cout << "no change break" << endl;
            break;
        }
    }
    for (int index = 0; index < length; ++index) {
        cout << array[index] << endl;
    }

}
