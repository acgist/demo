#include <iostream>

class Parent {
public:
    static int s;
    int a;
    void funa() {
        std::cout << "parent funa" << std::endl;
    }
    void funb(int) {
        std::cout << "parent funb" << std::endl;
    }
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

int Parent::s = 100;

class Son : public Parent {
public:
    static int s;
    void funa() {
        std::cout << "son funa" << std::endl;
    }
protected:
    int b;
private:
    int d;
public:
    Son()  {
        this->a = 11;
        // this->Parent::a;
        // this->b;
        // this->Parent::b;
        std::cout << "~Son" << this->a << std::endl;
    }
    ~Son()  {
        std::cout << "~Son" << this->a << std::endl;
    }
};

int Son::s = 200;

int main(int argc, char const *argv[]) {
    Parent a;
    Son b;
    b.a = 1000;
    b.Parent::a = 2000;
    b.funa();
    b.Parent::funa();
    b.funb(100);
    std::cout << "====" << b.a << std::endl;
    std::cout << "====" << b.Parent::a << std::endl;
    std::cout << "====" << Son::s << std::endl;
    std::cout << "====" << Parent::s << std::endl;
    std::cout << "====" << Son::Parent::s << std::endl;
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
