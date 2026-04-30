#include <list>
#include <vector>
#include <iostream>

class P {

public:
    P() {
        std::cout << "++++++P\n";
    }
    P(const P& p) {
        std::cout << "++++++P&\n";
    }
    P(const P&& p) {
        std::cout << "++++++P&\n";
    }
    ~P() {
        std::cout << "------P\n";
    }

};

int main() {
    std::list<P> list;
    // std::vector<P> list;
    // list.push_back(P{});
    // list.push_back(P{});
    list.emplace_back(P{});
    list.emplace_back(P{});
    std::cout << "======\n";
    std::list<P> copy;
    std::cout << copy.size() << " = " << list.size() << '\n';
    // copy.merge(copy.end(), list.begin(), list.end());
    // copy.merge(list);
    copy.splice(copy.end(), list);
    // copy.insert(copy.end(), list.begin(), list.end());
    std::cout << copy.size() << " = " << list.size() << '\n';
    list.push_back(P{});
    copy.splice(copy.end(), list);
    std::cout << copy.size() << " = " << list.size() << '\n';
    return 0;
}