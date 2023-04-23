#include <fstream>

int main(int argc, char const *argv[]) {
    std::fstream stream;
    stream.open("D:\\tmp\\c++.md", std::ios::in | std::ios::out | std::ios::app);
    stream
    << "1234哈哈"
    << std::endl
    << "1234";
    stream.flush();
    stream.close();
    return 0;
}
ls
