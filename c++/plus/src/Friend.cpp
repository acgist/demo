#include <iostream>

#define private public
class Person {

private:
    int age = 0;

};
#undef private

int getAge();

class FriendPerson : private Person {

friend int getAge();

};

int getAge() {
    Person person;
    std::cout << ((FriendPerson*)(&person))->age;
    return 1;
}

int main() {
    getAge();
    return 0;
}