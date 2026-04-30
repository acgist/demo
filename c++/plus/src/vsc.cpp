#include <stack>
#include <queue>
#include <deque>
#include <vector>
#include <iostream>

int main(int argc, char const *argv[]) {
    std::stack<int> s;
    s.push(1);
    s.push(2);
    s.push(3);
    std::cout << s.top() << s.size() << std::endl;
    s.pop();
    std::cout << s.top() << s.size() << std::endl;
    std::queue<int> q;
    q.push(1);
    q.push(2);
    q.push(3);
    std::cout << q.front() << q.back() << q.size() << std::endl;
    q.pop();
    std::cout << q.front() << q.back() << q.size() << std::endl;
    std::vector<int> v;
    v.push_back(1);
    v.push_back(2);
    v.push_back(3);
    std::cout << v.front() << v.back() << v.size() << std::endl;
    v.pop_back();
    std::cout << v.front() << v.back() << v.size() << std::endl;
    std::deque<int> d;
    d.push_back(1);
    d.push_back(2);
    d.push_back(3);
    d.push_front(4);
    d.push_front(5);
    d.push_front(6);
    std::cout << d.front() << d.back() << d.size() << std::endl;
    d.pop_back();
    d.pop_front();
    std::cout << d.front() << d.back() << d.size() << std::endl;
    return 0;
}
