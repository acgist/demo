#include <ctime>
#include <iostream>

int main() {

    // 水仙花数
    int abc = 100;
    int a, b, c;
    do {
        a = abc / 100;
        c = abc % 10;
        b = (abc / 10) % 10;
        if (a * a * a + b * b * b + c * c * c == abc) {
            std::cout << abc << std::endl;
        }
    } while (++abc < 1000);

    // 猜数
    srand((unsigned int) time(NULL));
    int in = 0;
    int num = rand() % 100 + 1;
    while (true) {
        std::cout << "输入数字：";
        std::cin >> in;
        if (in > num) {
            std::cout << "大了" << std::endl;
        } else if (in < num) {
            std::cout << "小了" << std::endl;
        } else {
            std::cout << "OK" << std::endl;
            break;
        }
    }

}
