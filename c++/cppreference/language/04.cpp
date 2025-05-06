#include <memory>
#include <iostream>

class A {

};

class B : public std::enable_shared_from_this<B> {

public:
    std::shared_ptr<B> get_ptr() {
        return this->shared_from_this();
    }

};

int main() {
    // A* a = new A;
    // std::shared_ptr<A> a_s_1(a);
    // std::shared_ptr<A> a_s_2(a);
    // // auto a_s_1 = std::make_shared<A>(a);
    // // auto a_s_2 = std::make_shared<A>(a);
    // std::cout << a_s_1.use_count() << '\n';
    // std::cout << a_s_2.use_count() << '\n';
    B* b = new B;
    std::shared_ptr<B> b_s_1(b);
    // std::shared_ptr<B> b_s_2(b);
    std::shared_ptr<B> b_s_3 = b->get_ptr();
    std::shared_ptr<B> b_s_4 = b->get_ptr();
    std::cout << b_s_1.use_count() << '\n';
    // std::cout << b_s_2.use_count() << '\n';
    std::cout << b_s_3.use_count() << '\n';
    std::cout << b_s_4.use_count() << '\n';
    return 0;
}