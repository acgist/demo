#include <queue>
#include <iostream>

int main() {
    std::priority_queue<int> queue;
    queue.emplace(2);
    queue.emplace(3);
    queue.emplace(1);
    std::cout << queue.top() << '\n';
    return 0;
}