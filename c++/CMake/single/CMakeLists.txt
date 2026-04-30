# mkdir build
# cd build
# MinGW需要：cmake -G "MinGW Makefiles" ..
# Linux环境：cmake ..
# make

# CMake最低版本要求
cmake_minimum_required(VERSION 2.8)

# 项目信息
project(single)

# 指定生成目标
add_executable(single src/Main.cpp)
