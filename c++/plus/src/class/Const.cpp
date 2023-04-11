#include <string>
#include <iostream>

// using std::cout;
// using std::endl;

using namespace std;

class Person {

public:
    int age;
    // 常函数中可以修改
    mutable int height;
    string name;
public:
    // this = Person * const this = 指针常量
    // 注意：const只能放在方法后面，表示修改this指针。
    // void say() {
    void say() const {
        // 不能修改
        // this->age = 100;
        this->height = 170;
        cout << this->age << endl;
        cout << this->height << endl;
    }

    string getName() {
        // this->name = "1234";
        return this->name;
    }

};

int main(int argc, char const *argv[]) {
    // Person * pPerson;
    Person* pPerson = new Person();
    // Person pPerson;
    pPerson->age = 100;
    pPerson->height = 160;
    pPerson->name = "1234";
    pPerson->say();
    cout << pPerson->getName() << endl;
    // pPerson.age = 100;
    // pPerson.height = 160;
    // pPerson.name = "1234";
    // pPerson.say();
    // cout << pPerson.getName() << endl;
    cout << "1234" << endl;
    const Person * pPersonConst;
    // 不能修改
    // pPersonConst->age = 100;
    pPersonConst->height = 170;
    // 只能调用常函数
    // pPersonConst->getName();
    return 0;
}
