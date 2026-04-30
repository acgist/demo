#include <iostream>

class P {
public:
    int a;
};

class M : public P {
public:
    int b;
};

void plus(P ps[], const int size) {
    // M* ms = (M*) (ps);
    // M* ms = static_cast<M*>(ps);
    for(int i = 0; i < size; ++i) {
        M m;
        m.a = i * 100;
        m.b = m.a;
        // ms[i] = m;
        ps[i] = m;
    }
}

int main() {
    M ps[4];
    plus(ps, 4);
    for(int i = 0; i < 4; ++i) {
        std::cout << ps[i].a << "\n";
    }
    return 0;
}