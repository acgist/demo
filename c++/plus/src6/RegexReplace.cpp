#include <regex>
#include <string>
#include <iostream>

int main() {
    std::string html = R"(
        <html>
            <a>link</a><p>text</p>
            <a>link</a><p>text</p>
            <a>link</a>
            <p>text</p>
        </html>
    )";
    std::regex regex(R"(<a>(.*)</a>\s*<p>(.*)</p>)");
    std::string target = "a = $1 p = $2";
    std::cout << "none = " << std::regex_replace(html, regex, target) << '\n';
    std::cout << "default = " << std::regex_replace(html, regex, target, std::regex_constants::format_default) << '\n';
    std::cout << "no_copy = " << std::regex_replace(html, regex, target, std::regex_constants::format_no_copy) << '\n';
    std::cout << "first_only = " << std::regex_replace(html, regex, target, std::regex_constants::format_first_only) << '\n';
    return 0;
}