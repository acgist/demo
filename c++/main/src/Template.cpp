#include <string>
#include <iostream>

template<class T>
// template<class T = std::string>
// template<typename T>
class Anime {
public:
    T name;
public:
    Anime() {
    }
    Anime(T name);
};

template<class T>
class Cat : public Anime<T> {

public:
    Cat(T name) : Anime<T>(name) {
    }
    // Cat(T name) {
    //     this->name = name;
    // }

    std::string say() {
        return this->name;
    }

};

template<class T>
Anime<T>::Anime(T name) {
    this->name = name;
}

int main(int argc, char const *argv[]) {
    Anime anime("测试");
    // Anime<std::string> anime("测试");
    std::cout << anime.name << std::endl;
    Cat cat("啾咪");
    // Cat<std::string> cat("啾咪");
    std::cout << cat.say() << std::endl;
    return 0;
}
