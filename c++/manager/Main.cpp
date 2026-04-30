// #include "locale.h"
#include "header/WorkerManager.hpp"

int main(int argc, char const *argv[]) {
    // std::setlocale(LC_ALL, "Chinese_China");
    WorkerManager workerManager;
    int choose = 0;
    while(true) {
        if(choose != 0) {
            system("pause");
            system("cls");
        }
        workerManager.showMenu();
        std::cout << "请输入菜单选择：";
        std::cin >> choose;
        switch(choose) {
            case 0:
            // exit(0);
            break;
            case 1:
            workerManager.addWorker();
            break;
            case 2:
            workerManager.showAllWorker();
            break;
            case 3:
            workerManager.modifyWorker();
            break;
            case 4:
            workerManager.deleteWorker();
            break;
            case 5:
            workerManager.selectWorker();
            break;
            case 6:
            workerManager.sortWorker();
            break;
            case 7:
            workerManager.saveWorker();
            break;
            case 8:
            workerManager.deleteAllWorker();
            break;
            default:
            system("cls");
            break;
        }
        if(choose == 0) {
            workerManager.exitSystem();
            break;
        }
    }
    return 0;
}
