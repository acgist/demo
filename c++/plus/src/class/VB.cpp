#include <string>
#include <iostream>

class Animel {

public:
    std::string* name;

public:
    virtual void miao() {
        std::cout << "我是Animel我叫" << *this->name << std::endl;
    }
    // void miao() {
    //     std::cout << "我是Animel我叫" << *this->name << std::endl;
    // }

Animel(std::string* name) : name(name) {
}

virtual ~Animel() {
    std::cout << "Animel析构" << std::endl;
    if(this->name != nullptr) {
        delete this->name;
    }
}
// ~Animel() {
//     std::cout << "Animel析构" << std::endl;
//     if(this->name != nullptr) {
//         delete this->name;
//     }
// }

};

class Cat : public Animel {

public:
    void miao() {
        std::cout << "我是Cat我叫" << *this->name << std::endl;
    }

Cat(std::string* name) : Animel(name) {
}

~Cat() {
    std::cout << "Cat析构" << std::endl;
}

};

int main(int argc, char const *argv[]) {
    std::string* name = new std::string("小白");
    // Cat cat(name);
    // cat.miao();
    Animel* animel = new Cat(name);
    animel->miao();
    delete animel;
    return 0;
}
