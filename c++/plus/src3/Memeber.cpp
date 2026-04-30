#include <iostream>

class N {

public:
    N() {
        std::cout << "N\n";
    }
private:
    virtual void say() {
        std::cout << "N.say\n";
    }
public:
    void sayProxy() {
        this->say();
    }

};

class P {

public:
    // 推荐使用指针
    N& n;
public:
    P(N& n) : n(n) {
    }

};

class NP : public N {

private:
    virtual void say() {
        std::cout << "NP.say\n";
    }

};

int main() {
    N n;
    P p(n);
    std::cout << &n << "\n";
    std::cout << &p.n << "\n";
    // P p(N{});
    // NP np;
    // np.sayProxy();
    N* np = new NP{};
    np->sayProxy();
    delete np;
    return 0;
}