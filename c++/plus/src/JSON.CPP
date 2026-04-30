#include <iostream>

#include "./json.hpp"

nlohmann::json get() {
    char chars[64] = {0};
    strcpy(chars, "{\"1\":\"1234\"}");
    // std::cout << chars << "1\n";
    nlohmann::json json = nlohmann::json::parse(chars);
    std::cout << json["1"]  << "\n";
    std::cout << &json["1"]  << "\n";
    return std::move(json);
}

int main() {
    char chars[64] = {0};
    strcpy(chars, "{\"1\":\"1234\"}");
    // std::cout << chars << "1\n";
    // nlohmann::json json = nlohmann::json::parse(chars);
    // std::cout << json.dump();
    // std::cout << &json  << "\n";
    // std::cout << json["1"]  << "\n";
    // std::cout << &json["1"]  << "\n";
    // nlohmann::json json2(json);
    nlohmann::json json2 = get();
    // nlohmann::json json2(std::move(json));
    // std::cout << &json2 << "\n";
    std::cout << json2["1"] << "\n";
    std::cout << &json2["1"] << "\n";

    return 0;
}