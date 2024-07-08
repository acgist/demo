#include <string>
#include <vector>
#include <iostream>

int main() {
    // using namespace std;
    using namespace std::literals;
    // using namespace std::string_literals;
    // using namespace std::literals::string_literals;
    auto a = "1234";
    auto b = "1234"s;
    auto c = {"1", "2", "3"};
    std::vector d {"1", "2", "3"};
    std::vector e {"1"s, "2"s, "3"s};
    std::string f = "1234"s;
    using namespace std::string_view_literals;
    auto g = "1234"sv;
    std::cout << R"(1234
    1234)" << '\n';
    return 0;
}