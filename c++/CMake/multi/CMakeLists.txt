# mkdir build
# cd build
# MinGW需要：cmake -G "MinGW Makefiles" ..
# Linux环境：cmake ..
# make

# CMake最低版本要求
cmake_minimum_required(VERSION 2.8)

# 项目信息
project(multi)

# 查找当前目录下的所有源文件
# 并将名称保存到DIR_SRCS变量
aux_source_directory(src DIR_SRCS)

# 指定生成目标
add_executable(multi ${DIR_SRCS})
