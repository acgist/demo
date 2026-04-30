#include <string>
#include <iostream>

class OverOp {

public:
    int age;
    // 方式一
public:
    OverOp& operator+ (OverOp &target) {
        OverOp* result = new OverOp();
        result->age = this->age + target.age;
        return *result;
    }

    OverOp& operator++() {
        this->age++;
        return *this;
    }

    const OverOp operator++(int) {
        OverOp temp = *this;
        this->age++;
        return temp;
    }

};

// 方式二
// OverOp* operator+ (OverOp &source, OverOp &target) {
//     OverOp* result = new OverOp();
//     result->age = source.age + target.age;
//     return result;
// }

std::ostream& operator<< (std::ostream &cout, const OverOp& op) {
    cout << "OverOp = " << op.age;
    return cout;
}

int main(int argc, char const *argv[]) {
    OverOp a;
    a.age = 1;
    OverOp b;
    b.age = 2;
    // 指针：不能用指针实现😀😀😀😀
    // 引用
    OverOp c = a + b;
    std::cout << c.age << std::endl;
    std::cout << c << std::endl;
    std::cout << ++c << std::endl;
    std::cout << c++ << std::endl;
    std::cout << c << std::endl;
    return 0;
}

