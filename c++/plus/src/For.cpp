#include <vector>
#include <cstring>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::vector<int> v;
    v.push_back(1);
    v.push_back(2);
    v.push_back(3);
    for(int value : v) {
        std::cout << value << " ";
        value = 1;
    }
    std::cout << std::endl;
    for(int& value : v) {
        std::cout << value << " ";
        value = 1;
    }
    std::cout << std::endl;
    for(int value : v) {
        std::cout << value << " ";
    }
    std::cout << std::endl;
    for(int x : { 1, 2, 3 }) {
        std::cout << x << std::endl;
    }
    for(const char* x : { "1", "2", "3" }) {
        std::cout << x << std::endl;
    }
    char c;
    char array[4];
    do {
        // std::cin >> c;
        std::cin.get(c);
        std::cout << c << std::endl;
        // std::cout << "input：";
        // std::cin.get(array, 4);
        // std::cin.clear();
        // std::cin.ignore();
        // std::cout << array << std::cin.fail() << std::cin.eof() << std::endl;
    // } while(c != '$');
    // } while(std::strcmp(array, "123") != 0);
    } while(!std::cin.fail());
    return 0;
}
