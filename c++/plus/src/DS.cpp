#include <iostream>

class User {
public:
    int id;
    User(int id) {
        this->id = id;
    }
    ~User() {
        std::cout << "析构函数" << std::endl;
    }
};

User GetUser() {
    User user(1);
    // User* userPtr = new User(1);
    // return *userPtr;
}

int main(int argc, char **argv) {
    User user = GetUser();
    return 0;
}