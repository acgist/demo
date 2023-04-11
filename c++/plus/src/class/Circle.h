// 防止重复编译方式一
#pragma once
// 防止重复编译方式二
//#ifndef CLASS_CIRCLE_H_
//#define CLASS_CIRCLE_H_

class Circle {
private:
protected:
public:
    // 半径
    int radius;
    // 计算周长
    double perimeter();
    // 有参和无参
    // 普通和拷贝
    Circle();
    Circle(int radius);
    // 继承
    virtual ~Circle();
    Circle(Circle &&other);
    Circle(const Circle &other);
    // 等号重载
//    Circle& operator=(Circle &&other);
//    Circle& operator=(const Circle &other);
};

//#endif
