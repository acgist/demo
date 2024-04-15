#pragma once

extern int a;
extern int b;

// static int a = 0;
// static int b = 0;

// inline int getA() {
//     return a;
// }

// inline int getB() {
//     return b;
// }

// extern int getA();
// extern int getB();

extern void print();

#include <iostream>

static int getA() {
    std::cout << __func__ << std::endl;
    ++a;
    return a;
}

static int getB() {
    ++b;
    return b;
}