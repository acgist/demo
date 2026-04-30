#include <iostream>

void fun(long long x, std::initializer_list<int> list) {
    for(auto iterator = list.begin(); iterator != list.end(); ++iterator) {
        std::cout << *iterator << "\n";
    }
}

void fun(int a) {
}

void fun(float a) {
}

class Weight {

public:
    int value = 0;

public:
    Weight() {
    }
    Weight(int value) {
        this->value = value;
    }
    ~Weight() {
    }

public:
    operator int() {
        return this->value;
    }
    // operator as_int() const {
    //     return this->value;
    // }

};

int main() {
    fun(100L, {});
    fun(100L, { 1, 2, 3 });
    fun(100L, { 1, 2, 3, 4 });
    fun(1);
    fun(1.0F);
    std::cout << "====\n";
    Weight w = 100;
    std::cout << w.value << "\n";
    int wv = w;
    std::cout << wv << "\n";
    return 0;
}