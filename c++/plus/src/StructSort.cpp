#include <string>
#include <iostream>

using namespace std;

#define MAX 100

struct Hero {
    string name;
    int age;
    string gender;
};

void sort(Hero heros[], int length) {
    for (int index = 0; index < length - 1; ++index) {
        for (int jndex = 0; jndex < length - index - 1; jndex++) {
            if(heros[jndex].age < heros[jndex + 1].age) {
                Hero temp = heros[jndex];
                heros[jndex] = heros[jndex + 1];
                heros[jndex + 1] = temp;
            }
        }
    }
}

int main(int argc, char **argv) {
//    Hero lb = {"刘备", 30, "男"};
//    Hero zf = {"张飞", 22, "男"};
//    Hero gy = {"关羽", 24, "男"};
//    Hero zy = {"赵云", 20, "男"};
//    Hero dc = {"貂蝉", 18, "女"};
//    Hero heros[] = {lb, zf, gy, zy, dc};
    Hero heros[] = {
        {"刘备", 30, "男"},
        {"张飞", 22, "男"},
        {"关羽", 24, "男"},
        {"赵云", 20, "男"},
        {"貂蝉", 18, "女"}
    };

    Hero *pHeros = heros;
    for (int index = 0; index < 5; index++) {
//        cout << heros[index].age << heras[index].name << endl;
        cout << pHeros->age << pHeros->name << endl;
        pHeros++;
    }
    sort(heros, 5);
    pHeros = heros;
    for (int index = 0; index < 5; index++) {
//        cout << heros[index].age << heras[index].name << endl;
        cout << pHeros->age << pHeros->name << endl;
        pHeros++;
    }

}

