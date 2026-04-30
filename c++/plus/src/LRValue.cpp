#include <iostream>
#include <vector>

class Data {

public:
    int value = 100;
    Data() {
        std::cout << "====1" << std::endl;
    };
    Data(const Data* data) {
        std::cout << "====2" << std::endl;
        this->value = data->value;
    };

};

class Object {
public:
    Object() {
        this->data = new Data();
    };
    Object(const Object& obj) {
        std::cout << "----1" << std::endl;
        this->data = new Data(obj.data);
    };
    Object& operator= (const Object& object) {
        std::cout << "--++2222" << std::endl;
        this->data = new Data(object.data);
        return *this;
    }
    // Object(Object&& obj) = default;
    Object(Object&& obj) {
        std::cout << "----2" << std::endl;
        this->data = new Data(obj.data);
    };
    ~Object() {
        if(this->data != nullptr) {
            delete this->data;
            this->data = nullptr;
        }
    }

public:
    Data* data;

};

class DD {

public:
    int a;
    DD() {
        std::cout << "++++1\n";
    }
    // DD(const DD& dd) = delete;
    DD(const DD& dd) : a(dd.a) {
        std::cout << "++++2\n";
    }
    // DD(DD&& dd) = delete;
    // DD(DD&& dd) = default;
    DD(DD&& dd) : a(dd.a) {
        std::cout << "++++3\n";
    }
    // DD(DD&& dd) : a(std::move(dd.a + 100)) {
    //     std::cout << "++++3\n";
    // }
    DD& operator=(DD&& dd) {
        this->a = dd.a;
        std::cout << "++++4\n";
        return *this;
    }
};

DD ddx() {
    DD dd;
    dd.a = 100;
    std::cout << "2==" << &dd << std::endl;
    std::cout << "2==" << &dd.a << std::endl;
    return dd;
    // return std::move(dd);
}

int intx() {
    int a = 100;
    std::cout << &a << std::endl;
    return a;
}

int main() {
    std::vector<Object> vector;
    vector.push_back(Object());
    std::cout << "========\n";
    Object object;
    vector.push_back(object);
    std::cout << vector.at(0).data->value << std::endl;
    std::cout << "========\n";
    Object copy = object;
    std::cout << (&copy)   << std::endl;
    std::cout << (&object) << std::endl;
    copy = object;
    std::cout << (&copy)   << std::endl;
    std::cout << (&object) << std::endl;
    std::cout << (copy.data)   << std::endl;
    std::cout << (object.data) << std::endl;
    std::cout << "========\n";
    DD dd;
    DD ddCopy = dd;
    std::cout << (&dd)     << std::endl;
    std::cout << (&ddCopy) << std::endl;
    std::cout << "========\n";
    DD returnDD = ddx();
    // DD&& returnDD = ddx();
    // const DD& returnDD = ddx();
    std::cout << returnDD.a << std::endl;
    std::cout << "1==" << &returnDD.a << std::endl;
    std::cout <<"1==" <<  &returnDD << std::endl;
    std::cout << "========\n";
    int intxx = intx();
    std::cout << &intxx << std::endl;
    const int& intxxRef = intx();
    std::cout << &intxxRef << std::endl;
    int&& intxxRefX = intx();
    std::cout << &intxxRefX << std::endl;
    return 0;
}
