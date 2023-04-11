#include "Circle.h"

#include <iostream>

#define PI 3.14;

Circle::Circle() {
    std::cout << "init" << std::endl;
}

Circle::Circle(int radius) {
    std::cout << "init radius" << std::endl;
    this->radius = radius;
}

Circle::~Circle() {
    std::cout << "destory" << std::endl;
}

Circle::Circle(Circle &&other) {
    std::cout << "copy &&" << std::endl;
    this->radius = 10;
}

Circle::Circle(const Circle &other) {
    std::cout << "copy &" << std::endl;
}

//Circle& Circle::operator=(Circle &&other) {
//    return *this;
//}

//Circle& Circle::operator=(const Circle &other) {
//    return *this;
//}

double Circle::perimeter() {
    return this->radius * 2 * PI;
}

// 调用拷贝构造函数
void copy(Circle c) {

}

// 调用拷贝构造函数
/**
 * 添加编译参数：-fno-elide-constructors
 * 同时打开构造：Circle::Circle(Circle &&other)
 */
Circle clone() {
    using namespace std;
    Circle circle;
    cout << &circle << endl;
    return circle;
}

void callClone() {
    using namespace std;
    Circle cloneCircle = clone();
    cout << &cloneCircle << endl;
    cout << cloneCircle.perimeter() << endl;
    cout << cloneCircle.perimeter() << endl;
}

int main(int argc, char **argv) {
    using namespace std;
//  Circle circle(20);
//  Circle circle;
//  Circle circle = Circle();
//  Circle(20); // 匿名对象：结束以后立即回收；不要用拷贝函数初始化匿名对象；
//  Circle(circle); // 编译器会认为这是一个声明
    // 无参构造不能这样写`Circle circle();`，编译器会认为这是一个函数。
//  Circle circle = Circle(10);
//  circle.radius = 10;
//  映射转换：Circle circle = Circle(20);
//    Circle circle = 20;
//    cout << circle.perimeter() << endl;
//    copy(circle);
//    Circle cloneCircle = clone();
//    cout << &cloneCircle << endl;
//    cout << cloneCircle.perimeter() << endl;
//    cout << cloneCircle.perimeter() << endl;
    callClone();
    return 0;
}
