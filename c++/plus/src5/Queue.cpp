#include <queue>
#include <iostream>

int main() {
    std::queue<int> queue;
    queue.emplace(1);
    queue.emplace(2);
    std::cout << queue.size() << '\n';
    std::cout << queue.front() << '\n';
    std::cout << queue.back() << '\n';
    queue.pop();
    std::cout << queue.size() << '\n';
    return 0;
}