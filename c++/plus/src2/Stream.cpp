#include <fstream>
#include <iostream>

int main() {
    std::ifstream input;
    std::ofstream output;
    input.open("D:/tmp/input.txt", std::ios::in | std::ios::binary);
    output.open("D:/tmp/output.txt", std::ios::trunc | std::ios::binary);
    if(input.is_open() && output.is_open()) {
        int length = 0;
        char chars[1024];
        while(input >> chars) {
        // while(input.read(chars, 1024)) {
        // while((length = input.readsome(chars, 1024))) {
            std::cout << chars << input.gcount() << "\n";
            // output.write(chars, length);
            // output.write(chars, sizeof(chars));
            output.write(chars, input.gcount());
        }
    }
    input.close();
    output.close();
    return 0;
}