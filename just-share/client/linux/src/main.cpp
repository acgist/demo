# include "jshare/cli.hpp"

#include <filesystem>

int main(int argc, char const *argv[]) {
    jshare::init(std::filesystem::absolute(std::filesystem::path("./")).string(), argc, argv);
    jshare::kyin();
    return 0;
}
