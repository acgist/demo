#include <cassert>
#include <iostream>

#pragma warning(disable:1234)

void fun() {
    // static_assert(1 == 1);
    // static_assert(1 == 0);
    assert(1 == 1);
    assert(1 == 0);
}

int main() {
    fun();
    return 0;
}