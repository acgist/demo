#include <iostream>
#include <filesystem>

int main(const int argc, const char *const argv[]) {
    std::filesystem::path path{argv[0]};
    std::cout << argv[0] << '\n';
    std::cout << std::filesystem::u8path(argv[0]).parent_path().string() << '\n';
    std::cout << std::filesystem::u8path(argv[0]).relative_path().string() << '\n';
    std::cout << std::filesystem::absolute(".") << '\n';
    std::cout << std::filesystem::absolute(std::filesystem::u8path(argv[0]).parent_path()) << '\n';
    std::cout << std::filesystem::absolute(std::filesystem::u8path(argv[0]).parent_path()).parent_path() << '\n';
    return 0;
}
