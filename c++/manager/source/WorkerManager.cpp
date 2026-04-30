#include "stdlib.h"
#include "string.h"
#include "../header/WorkerManager.hpp"

Worker::Worker(int no, std::string name, std::string dept) : no(no), name(name), dept(dept) {
}

Worker::~Worker() {
}

void Worker::print() {
    std::cout <<
    "员工编号：" << this->no   << "\t" <<
    "员工名称：" << this->name << "\t" <<
    "员工部门：" << this->dept << std::endl;
}

WorkerManager::WorkerManager() {
    std::ifstream stream;
    stream.open(WORKERS_LIST_FILE, std::ifstream::in);
    // char ch;
    // stream >> ch;
    // if(stream.eof()) {
    //     // 空文件
    // }
    if(stream.is_open()) {
        // char buffer[1024] = { 0 };
        // while(stream.getline(buffer, sizeof(buffer))) {
        //     if(strlen(buffer) <= 0) {
        //         continue;
        //     }
        //     const char* no   = strtok(buffer, "\t");
        //     const char* name = strtok(NULL, "\t");
        //     const char* dept = strtok(NULL, "\t");
        //     if(no != nullptr && name != nullptr && dept != nullptr) {
        //         Worker worker(atoi(no), name, dept);
        //         this->workers.push_back(worker);
        //     }
        // }

        int no;
        std::string name;
        std::string dept;
        while(
            stream >> no   &&
            stream >> name &&
            stream >> dept
        ) {
            Worker worker(no, name, dept);
            this->workers.push_back(worker);
        }
    } else {
        std::cout << "没有文件" << std::endl;
    }
    stream.close();
}

WorkerManager::~WorkerManager() {
}

void WorkerManager::showMenu() {
    std::cout << "****************************************" << std::endl;
    std::cout << "********** 欢迎使用职工管理系统 ********" << std::endl;
    std::cout << "*********** 0. 退出管理程序     ********" << std::endl;
    std::cout << "*********** 1. 新增职工信息     ********" << std::endl;
    std::cout << "*********** 2. 显示职工信息     ********" << std::endl;
    std::cout << "*********** 3. 修改职工信息     ********" << std::endl;
    std::cout << "*********** 4. 删除职工信息     ********" << std::endl;
    std::cout << "*********** 5. 查找职工信息     ********" << std::endl;
    std::cout << "*********** 6. 排序职工信息     ********" << std::endl;
    std::cout << "*********** 7. 保存职工信息     ********" << std::endl;
    std::cout << "*********** 8. 清空职工信息     ********" << std::endl;
    std::cout << "****************************************" << std::endl;
}

void WorkerManager::exitSystem() {
    std::cout << "欢迎下次使用" << std::endl;
}

void WorkerManager::addWorker() {
    std::cout << "输入员工编号：";
    int no;
    std::cin >> no;
    std::cout << "输入员工名称：";
    std::string name;
    std::cin >> name;
    std::cout << "输入员工部门：";
    std::string dept;
    std::cin >> dept;
    Worker worker(no, name, dept);
    this->workers.push_back(worker);
}

void WorkerManager::showAllWorker() {
    std::list<Worker>::iterator iterator = this->workers.begin();
    while(iterator != this->workers.end()) {
        Worker worker = *iterator;
        worker.print();
        iterator++;
    }
}

void WorkerManager::modifyWorker() {
    std::cout << "输入修改员工编号：";
    int no;
    std::cin >> no;
    std::list<Worker>::iterator iterator = this->workers.begin();
    Worker* modify = nullptr;
    while(iterator != this->workers.end()) {
        if(iterator->no == no) {
            modify = &(*iterator);
            break;
        }
        iterator++;
    }
    if(modify == nullptr) {
        std::cout << "没有找到员工：" << no << std::endl;
    } else {
        std::cout << "输入员工名称：";
        std::string name;
        std::cin >> name;
        modify->name = name;
        std::cout << "输入员工部门：";
        std::wstring dept;
        std::wcin >> dept;
        std::wcout << dept << std::endl;
        // modify->dept = dept;
        std::cout << "修改成功" << std::endl;
    }
}

void WorkerManager::deleteWorker() {
    std::cout << "输入删除员工编号：";
    int no;
    std::cin >> no;
    std::list<Worker>::iterator iterator = this->workers.begin();
    while(iterator != this->workers.end()) {
        if(iterator->no == no) {
            this->workers.erase(iterator);
            break;
        }
        iterator++;
    }
}

void WorkerManager::selectWorker() {
}

void WorkerManager::sortWorker() {
}

void WorkerManager::saveWorker() {
    std::ofstream stream;
    stream.open(WORKERS_LIST_FILE, std::ofstream::out);
    // stream.open(WORKERS_LIST_FILE, std::ofstream::out | std::ios::app);
    if(stream.is_open()) {
        std::list<Worker>::iterator iterator = this->workers.begin();
        while(iterator != this->workers.end()) {
            stream <<
            iterator->no   << "\t" <<
            iterator->name << "\t" <<
            iterator->dept << "\n";
            iterator++;
        }
    }
    stream.close();
    std::cout << "保存成功" << std::endl;
}

void WorkerManager::deleteAllWorker() {
}
