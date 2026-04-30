#include <array>
#include <iostream>

struct Person {

public:
    int age;
    std::string name;
    Person(int age, std::string name) : age(age), name(name) {
    };

};

class Anime {

public:
    int age;
    std::string name;
    Anime(int age, std::string name) : age(age), name(name) {
    };

};

constexpr int loopCount(int n) {
    return n + 1;
}

int main(int argc, char const *argv[]) {
    Person person{ 1, "person" };
    Anime anime(2, "anime");
    auto& [a, n] = person;
    std::cout << a << '\n';
    a = 22;
    std::cout << person.age << std::endl;
    auto [x, xx] = anime;
    std::cout << x << std::endl;
    x = 22;
    std::cout << anime.age << std::endl;
    std::cout << loopCount(2) << std::endl;
    std::array<int, 2> array;
    array[0] = 1111;
    const auto& [a1, b1] = array;
    std::cout << a1 << std::endl;
    return 0;
}
