#include <iostream>

using namespace std;

int main(int argc, char **argv) {
    int array1[2][3];
    int array2[2][3] = {
        { 1, 2, 3 },
        { 4, 5, 6}
    };
    int array3[2][3] = { 1, 2, 3, 4, 5, 6 };
    int array4[][3] = { 1, 2, 3, 4, 5, 6 };
//  int array5[2][] = { 1, 2, 3, 4, 5, 6 };
    for(int i = 0; i < 2; i++) {
        for(int j = 0; j < 2; j++) {
            cout << array4[i][j] << " ";
        }
        cout << endl;
    }
    cout << array4 << endl;
    cout << &array4 << endl;
    cout << array4[0] << endl;
    cout << &array4[0] << endl;
    cout << array4[0][0] << endl;
    cout << &array4[0][0] << endl;
}
