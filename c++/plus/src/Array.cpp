#include <iostream>

using namespace std;

int main() {
    /*
     * 不能这样定义：int[5] array;
     */
    int arrayA[5];
    // 这种默认填充零
    int arrayB[5] = { 1, 2, 3, 4 };
    // 默认设置长度
    int arrayC[] = { 1, 2, 3, 4, 5 };
    cout << arrayA[0] << endl;
    cout << arrayB[0] << endl;
    cout << arrayC[0] << endl;
    arrayA[0] = 1;
    cout << arrayA[0] << endl;
    cout << arrayB[0] << endl;
    cout << arrayC[0] << endl;
    cout << "循环输出" << endl;
    int size = sizeof(arrayA) / sizeof(arrayA[0]);
    for (int index = 0; index < size; index++) {
        cout << "索引" << index << endl;
        cout << arrayA[index] << endl;
        cout << arrayB[index] << endl;
        cout << arrayC[index] << endl;
    }
    return 0;
}
