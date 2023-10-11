#include <vector>
#include <numeric>
#include <iostream>
#include <algorithm>

using std::cout;
using std::endl;
using std::vector;

int main(int argc, char const *argv[]) {
    vector<int> vector;
    vector.push_back(1);
    vector.push_back(2);
    vector.push_back(3);
    vector.push_back(0);
    std::for_each(vector.begin(), vector.end(), [](int& value) {
        cout << value << endl;
    });
    cout << endl;
    std::replace(vector.begin(), vector.end(), 1, 100);
    std::for_each(vector.begin(), vector.end(), [](int& value) {
        cout << value << endl;
    });
    cout << endl;
    std::replace_if(vector.begin(), vector.end(), [](int& value) {
        return value < 100;
    }, 10000);
    std::for_each(vector.begin(), vector.end(), [](int& value) {
        cout << value << endl;
    });
    std::vector<int> swap;
    cout << swap.size() << " = " << vector.size() << endl;
    std::swap(swap, vector);
    cout << swap.size() << " = " << vector.size() << endl;
    cout << std::accumulate(swap.begin(), swap.end(), 10) << endl;
    vector.resize(10);
    std::fill(vector.begin(), vector.end(), 1);
    cout << std::accumulate(vector.begin(), vector.end(), 10) << endl;
    return 0;
}
