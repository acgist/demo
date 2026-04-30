# mkdir build
# cd build
# MinGW需要：cmake -G "MinGW Makefiles" ..
# Linux环境：cmake ..
# make

# CMake最低版本要求
cmake_minimum_required(VERSION 2.8)

# 项目信息
project(manager)

# 查找当前目录下的所有源文件
aux_source_directory(header HEADER_SRC)
aux_source_directory(source SOURCE_SRC)

# 指定生成目标
add_executable(manager Main.cpp ${HEADER_DIR} ${SOURCE_DIR})
