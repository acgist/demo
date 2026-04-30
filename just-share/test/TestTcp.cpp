#include "jshare/tcp.hpp"
#include "jshare/udp.hpp"

#include <chrono>
#include <thread>

int main() {
    jshare::socket::init();
    std::thread t1([]() {
        jshare::tcp::server();
    });
    std::thread t2([]() {
        jshare::tcp::client();
    });
    std::this_thread::sleep_for(std::chrono::days(7));
    return 0;
}