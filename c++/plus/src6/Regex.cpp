#include <regex>
#include <iostream>

int main() {
    // ^&\.*+?()[]{}|
    std::regex regex("a.c");
    std::cout << std::regex_match("ac",   regex) << '\n';
    std::cout << std::regex_match("abc",  regex) << '\n';
    std::cout << std::regex_match("0abc", regex) << '\n';
    std::cout << std::regex_match("abbc", regex) << '\n';
    std::cout << std::regex_match("[]", std::regex("\\[\\]")) << '\n';
    std::cout << std::regex_match("[]", std::regex(R"(\[\])")) << '\n';
    std::cout << std::regex_match("123-abc-123", std::regex(R"((\d+)-abc-\1)")) << '\n';
    std::cout << std::regex_match("123-abc-1234", std::regex(R"((\d+)-abc-\1)")) << '\n';
    std::cout << std::regex_match("123-abc-1234", std::regex(R"((\d+)-abc-(\1)4)")) << '\n';
    // 提取
    // std::cout << std::regex_match("a", std::regex(R"(a(?!b))")) << '\n';
    // std::cout << std::regex_match("a", std::regex(R"(a(?=b))")) << '\n';
    // std::cout << std::regex_match("a", std::regex(R"(a(?!b))")) << '\n';
    // std::cout << std::regex_match("a", std::regex(R"(a(?=b))")) << '\n';
    return 0;
}