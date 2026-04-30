#include <cassert>
#include <iostream>

#pragma warning(disable:1234)

void fun(int age) {
    // [[notthrow]]
    // static_assert(1 == 1, "message");
    // static_assert(1 == 0, "message");
    assert(1 == 1);
    assert(1 == 0);
}

int main() {
    fun(1);
    return 0;
}