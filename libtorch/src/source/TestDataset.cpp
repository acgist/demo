#include "../header/Datasets.hpp"

int main() {
    int rows = 0;
    int cols = 0;
    auto csv  = lifuren::datasets::loadCSV("D:/tmp/house/all.csv");
    // auto csv  = lifuren::datasets::loadCSV("D:/tmp/house/train.csv");
    auto mark = lifuren::datasets::mark(csv, rows, cols);
    auto testCSV  = lifuren::datasets::loadCSV("D:/tmp/house/test.csv");
    auto testMark = lifuren::datasets::mark(testCSV, rows, cols, true, true, 1, 1, 0);
    return 0;
}