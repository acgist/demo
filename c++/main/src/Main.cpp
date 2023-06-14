/**
 * Main两种标准写法
 */

#include <iostream>

/**
 * 无参
 */
//int main() {
//
//}

int main(int argc, char **argv) {
	system("chcp 65001");
	std::cout << "Hello World" << std::endl;
	std::cout << "中文测试" << std::endl;
	int  a{100};
	int  b(101);
	long c{100L};
	long d;
	std::cout << a << std::endl;
	std::cout << b << std::endl;
	std::cout << c << std::endl;
	std::cout << d << std::endl;
	return 0;
}
