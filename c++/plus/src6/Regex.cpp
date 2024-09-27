#include <regex>
#include <iostream>

int main() {
    // ^&\.*+?()[]{}|
    // std::regex regex("a.c");
    // std::cout << std::regex_match("ac",   regex) << '\n';
    // std::cout << std::regex_match("abc",  regex) << '\n';
    // std::cout << std::regex_match("0abc", regex) << '\n';
    // std::cout << std::regex_match("abbc", regex) << '\n';
    // std::cout << std::regex_match("[]", std::regex("\\[\\]")) << '\n';
    // std::cout << std::regex_match("[]", std::regex(R"(\[\])")) << '\n';
    // std::cout << std::regex_match("123-abc-123", std::regex(R"((\d+)-abc-\1)")) << '\n';
    // std::cout << std::regex_match("123-abc-1234", std::regex(R"((\d+)-abc-\1)")) << '\n';
    // std::cout << std::regex_match("123-abc-1234", std::regex(R"((\d+)-abc-(\1)4)")) << '\n';
    // 提取
    // std::cout << std::regex_match("a", std::regex(R"(a(?!b))")) << '\n';
    // std::cout << std::regex_match("a", std::regex(R"(a(?=b))")) << '\n';
    // std::cout << std::regex_match("a", std::regex(R"(a(?!b))")) << '\n';
    // std::cout << std::regex_match("a", std::regex(R"(a(?=b))")) << '\n';
    // match
    // std::smatch mret;
    // std::string str = "123-abc-123";
    // if(std::regex_match(str, mret, std::regex(R"(\d+-(\w+)-\d+)"))) {
    //     std::cout << mret.size() << '\n';
    //     std::cout << mret[0] << '\n';
    //     std::cout << mret[1] << '\n';
    //     std::cout << mret[2] << '\n';
    // }
    // search
    // std::smatch mret;
    // std::string str = "123-abc-123";
    // if(std::regex_search(str, mret, std::regex(R"(\d+)"))) {
    //     std::cout << mret.size() << '\n';
    //     std::cout << mret[0] << '\n';
    //     std::cout << mret[1] << '\n';
    //     std::cout << mret[2] << '\n';
    // }
    // search iterator
    // std::string str = "123-abc-123";
    // std::regex reg(R"(\d+)");
    // std::sregex_iterator end;
    // std::sregex_iterator beg(std::cbegin(str), std::cend(str), reg);
    // for(; beg != end; ++beg) {
    //     std::cout << (*beg)[0] << '\n';
    // }
    // search token iterator
    std::string str = "123-abc-123";
    std::regex reg(R"(\d+)");
    std::sregex_token_iterator end;
    std::sregex_token_iterator beg(std::cbegin(str), std::cend(str), reg);
    for(; beg != end; ++beg) {
        std::cout << (*beg) << '\n';
        std::cout << beg->str() << '\n';
    }
    return 0;
}