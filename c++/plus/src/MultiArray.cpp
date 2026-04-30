#include <string>
#include <iostream>

void print(int (*array)[3], int row, int col);
// void print(int (*array)[3], int row, int col);
// void print(int array[][3], int row, int col);

char* fill(char x, int n);

int main() {
    int array[2][3] = {
        {1, 2, 3},
        {4, 5, 6}
    };
    print(array, 2, 3);
    if("xx") {
        std::cout << "xx" << std::endl;
    }
    if("") {
        std::cout << "empty" << std::endl;
    }
    if(-100) {
        std::cout << "-100" << std::endl;
    }
    char x;
    int  n;
    std::cin >> x;
    while(!(std::cin >> n)) {
        std::cin.clear();
        // std::cin.sync();
        std::cin.ignore();
        std::cout << "输入正确数字" << std::endl;
    }
    const char* v = fill(x, n);
    std::cout << v << std::endl;
    delete[] v;
    return 0;
}

void print(int (*array)[3], int row, int col) {
    for(int index = 0; index < row; index++) {
        for(int jndex = 0; jndex < col; jndex++) {
            std::cout << array[index][jndex];
        }
        std::cout << std::endl;
        std::cout << "====" << std::endl;
    }
}

char* fill(char x, int n) {
    char* v = new char[n + 1];
    v[n] = '\0';
    while(n-- > 0) {
        v[n] = x;
    }
    return v;
}