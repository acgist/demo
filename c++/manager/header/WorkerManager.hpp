#pragma once

#include <list>
#include <string>
#include <fstream>
#include <iostream>

#ifndef WORKERS_LIST_FILE
#define WORKERS_LIST_FILE "workers.list"
#endif

class Worker {

public:
    /**
     * 职工编号
     */
    int         no;
    /**
     * 职工名称
     */
    std::string name;
    /**
     * 职工部门
     */
    std::string dept;
public:
    Worker(int no, std::string name, std::string dept);
    virtual ~Worker();
public:
    void print();

};

class WorkerManager {

public:
    WorkerManager();
    virtual ~WorkerManager();
public:
    std::list<Worker> workers;
    // 使用数组需要用到指针
    // Worker ** workers;
    // Worker *workers[];
public:
    void showMenu();
    void exitSystem();
    void addWorker();
    void showAllWorker();
    void modifyWorker();
    void deleteWorker();
    void selectWorker();
    void sortWorker();
    void saveWorker();
    void deleteAllWorker();

};