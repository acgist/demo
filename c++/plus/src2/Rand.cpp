#include <random>

class Price {

public:
    float a;
    float b;
    float c;

};

static const int MAX = 10000;
static Price prices[MAX];

int main() {
    std::random_device device;
    std::mt19937 rand(device());
    std::normal_distribution<> normal(100, 10);
    std::uniform_int_distribution<> uniform(500, 1000);
    for(int i = 0; i < MAX; ++i) {
        auto& x = prices[i];
        x.a = uniform(rand);
        x.b = uniform(rand);
        // 5.8 * a + 87.4 * b + 1024 + rand = c
        x.c = x.a * 5.8 + x.b * 87.4 + 1024 + normal(rand);
    }
    return 0;
}