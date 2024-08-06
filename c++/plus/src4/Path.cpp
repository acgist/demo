#include <iostream>
#include <filesystem>

int main() {
    std::filesystem::path path{ R"(D:\0/)" };
    path.append("1");
    path /= "2";
    path.concat("3");
    path += "4";
    std::cout << path << '\n';
    std::cout << path.parent_path() << '\n';
    std::cout << path.filename() << '\n';
    std::filesystem::space_info info{ std::filesystem::space("D:\\tmp") };
    std::cout << info.available << '\n';
    return 0;
}