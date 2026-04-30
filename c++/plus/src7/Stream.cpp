#include <fstream>
#include <iostream>

int read(std::ifstream& stream) {
    char chars[1];
    if(stream.read(chars, 1)) {
        return chars[0];
    }
    return 0;
}

int main() {
    std::ifstream stream("D:/tmp/input.txt");
    for(int i = 0; i < 10; ++i) {
        int a = read(stream);
        std::cout << a << " = " << stream.eof() << '\n';
    }
    return 0;
}
