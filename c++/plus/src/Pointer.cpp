#include <iostream>

using namespace std;

int main() {
    int array[] = { 1, 4, 8, 16 };
    std::cout << *array << std::endl;
    std::cout << *array + 1 << std::endl;
    std::cout << *(array + 1) << std::endl;
    std::cout << &array << std::endl;
    // &array是整个数组的地址
    std::cout << &array + 1 << std::endl;
    std::cout << array + 3 << std::endl;
    std::cout << *(array + 3) << std::endl;
    std::cout << array + 4 << std::endl;
    cout << "指针常量" << endl;
    int a = 10;
    // const int a = 10; // 不能这样定义
    const int b = 10;
    int *const pA = &a;
    cout << pA << endl;
    cout << *pA << endl;
//  不能修改
//  pA = &b;
    *pA = 20;
    cout << pA << endl;
    cout << *pA << endl;
    // int b = 10;
    cout << "常量指针" << endl;
    int const *pB = &b;
    cout << pB << endl;
    cout << *pB << endl;
    const int t = 20;
    pB = &t;
//  不能修改
//  *pB = 30;
    cout << pB << endl;
    cout << *pB << endl;
    cout << sizeof(pB) << endl;
    cout << sizeof(*pB) << endl;
    cout << sizeof(int *) << endl;
}
