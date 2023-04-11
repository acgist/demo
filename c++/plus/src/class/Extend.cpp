#include <iostream>

class Parent {
public:
    int a;

protected:
    int b;

private:
    int c;
public:
    Parent()  {
        this->a = 10;
        std::cout << "~Parent" << this->a << std::endl;
    }
    ~Parent()  {
        std::cout << "~Parent" << this->a << std::endl;
    }
};

class Son : public Parent {
protected:
    int b;
private:
    int d;
public:
    Son()  {
        this->a = 11;
        // this->b;
        // this->Parent::b;
        std::cout << "~Son" << this->a << std::endl;
    }
    ~Son()  {
        std::cout << "~Son" << this->a << std::endl;
    }
};

int main(int argc, char const *argv[]) {
    Parent a;
    Son b;
    std::cout << sizeof(a) << std::endl;
    std::cout << sizeof(b) << std::endl;
    // std::cout << sizeof(long) << std::endl;
    // std::cout << sizeof(long long) << std::endl;
    // Parent* c = new Parent();
    // Son* d = new Son();
    // delete c;
    // delete d;
    Parent* e = new Son();
    delete e;
    // std::string* f = new std::string("1234");
    // delete f;
    // 错误释放
    // const char* f = "1234";
    // delete[] f;
    std::cout << "OK" << std::endl;
    return 0;
}
