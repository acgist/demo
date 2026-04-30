#include <list>
#include <iostream>
#include <algorithm>

int main() {
    typedef std::list<int>::iterator iterator;
    std::list<int> list;
    list.push_back(1);
    list.push_front(2);
    iterator beginPtr = list.begin();
    iterator endPtr   = list.end();
    std::for_each(beginPtr, endPtr, [](int& value) {
        std::cout << value << std::endl;
        value = 100;
    });
    for(; beginPtr != endPtr; beginPtr++) {
        std::cout << *beginPtr << std::endl;
    }
    return 0;
}
