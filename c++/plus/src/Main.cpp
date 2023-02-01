//int main(void) {
//	return 0;
//}

#include <iostream>

#include "Clazz.h"

using std::cout;
using std::endl;
//using namespace std;

/*
 * 启动函数
 */
int main(int argc, char **argv) {
//	using namespace std;
	Clazz clazz;
	Clazz* clazzNew = new Clazz();
	clazzNew->say();
	delete clazzNew;
	clazz.say();
	Clazz* pClazz = &clazz;
	pClazz->say();
	(*pClazz).say();
//	Clazz* pClazz;
//	pClazz = &clazz;
//	pClazz->say();
	cout << "namespace" << endl;
	cout << &"namespace" << endl;
	cout << *&"namespace" << endl;
	cout << &"namespace" << endl;
	cout << *&"namespace" << endl;
}
