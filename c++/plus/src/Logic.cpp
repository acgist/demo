#include <iostream>

using namespace std;

int main() {
    int score;
    do {
        cout << "请输入分数：";
        cin >> score;
        cout << "当前分数（大于600退出）：" << score << endl;
    } while (score <= 600);
    return 0;
}
