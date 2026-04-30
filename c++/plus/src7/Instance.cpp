#include <mutex>
#include <iostream>

static std::mutex mutex;

class Single {

public:
    static Single& getInstance() {
        // static Single instance;
        // return instance;
        // static std::once_flag flag;
        // std::call_once(flag, []() {
        //     Single::instance = new Single();
        // });
        // return *Single::instance;
        if(!Single::instance) {
            std::lock_guard<std::mutex> lock(mutex);
            if(!Single::instance) {
                Single::instance = new Single;
            }
        }
        return *Single::instance;
    }
    int get() {
        return 0;
    }
private:
    static Single* instance;

};

Single* Single::instance = nullptr;
// Single* Single::instance = new Single;

int main() {
    auto& instance = Single::getInstance();
    std::cout << instance.get();
    return 0;
}