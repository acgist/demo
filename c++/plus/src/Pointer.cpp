#include <iostream>

using namespace std;

int main() {
	cout << "指针常量" << endl;
	int a = 10;
	// const int a = 10; // 不能这样定义
	int* const pA = &a;
	cout << pA << endl;
	cout << *pA << endl;
	*pA = 20;
	cout << pA << endl;
	cout << *pA << endl;
	// int b = 10;
	cout << "常量指针" << endl;
	const int b = 10;
	int const* pB = &b;
	cout << pB << endl;
	cout << *pB << endl;
	const int t = 20;
	pB = &t;
	cout << pB << endl;
	cout << *pB << endl;
}