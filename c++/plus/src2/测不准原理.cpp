#include <iostream>

int main() {
    int i = 0;
    printf("测试：%d %d\n", i, ++i);
    i = 0;
    printf("测试：%d %d\n", i, i++);
    i = 0;
    printf("测试：%d %d\n", i, i += 1);
}