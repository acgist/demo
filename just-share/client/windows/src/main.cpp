# include "jshare/cli.hpp"

#include <cstdlib>
#include <filesystem>

int main(int argc, char const *argv[]) {
    #if _WIN32
    std::system("chcp 65001");
    #endif
    jshare::init(std::filesystem::absolute(std::filesystem::path("./")).string(), argc, argv);
    jshare::kyin();
    return 0;
}
