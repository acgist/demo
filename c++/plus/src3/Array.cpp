#include <vector>
#include <iostream>

class P {
public:
    P() {
    }
    ~P() {
        std::cout << "~\n";
    }
};

int main() {
    // P* p = new P[10];
    P** p = new P*[10];
    for (int i = 0; i < 10; ++i) {
        // p[i] = new P{};
        // p[i] = *(new P{});
        p[i] = new P[10];
        for(int j = 0; j < 10; ++j) {
            p[i][j] = *(new P{});
        }
    }
    // delete[] p;
    for (int i = 0; i < 10; ++i) {
        delete[] p[i];
    }
    delete[] p;
    // std::vector<P*> ps;
    // ps.push_back(new P{});
    // ps.push_back(new P{});
    // ps.push_back(new P{});
    // ps.push_back(new P{});
    return 0;
}