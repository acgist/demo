#include <iostream>
#include <filesystem>

int main() {
    std::cout << std::filesystem::is_regular_file("D:/tmp") << std::endl;
    std::cout << std::filesystem::is_directory("D:/tmp") << std::endl;
    std::cout << std::filesystem::is_regular_file("D:/tmp/file.txt") << std::endl;
    std::cout << std::filesystem::is_directory("D:/tmp/file.txt") << std::endl;
    std::cout << std::filesystem::is_regular_file("D:/tmp/xx") << std::endl;
    std::cout << std::filesystem::is_directory("D:/tmp/xx") << std::endl;
    return 0;
}