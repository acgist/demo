# mkdir build
# cd build
# MinGW需要：cmake -G "MinGW Makefiles" ..
# Linux环境：cmake ..
# make

# set(CMAKE_C_COMPILER C:/develop/msys64/mingw64/bin/gcc.exe)
# set(CMAKE_CXX_COMPILER C:/develop/msys64/mingw64/bin/g++.exe)

# CMake最低版本要求
cmake_minimum_required(VERSION 3.25)

# 项目信息
project(study)

include_directories(D:/develop/libtorch/include)
include_directories(D:/develop/libtorch/include/torch/csrc/api/include)

file(GLOB PYTORCH_DLLS "D:/develop/libtorch/include/lib/*.dll")
file(GLOB PYTORCH_LIBS "D:/develop/libtorch/include/lib/*.lib")

message(STATUS "pytorch dlls: ${PYTORCH_DLLS} ")
message(STATUS "pytorch dlls: ${PYTORCH_LIBS} ")

# add_library(libtorchShared SHARED D:/develop/libtorch/lib)
# add_library(libtorchStatic STATIC D:/develop/libtorch/lib)

link_directories(D:/develop/libtorch/lib)

# 指定生成目标
add_executable(study src/Main.cpp)

# target_link_libraries(study asmjit c10 fbgemm fbjni libiomp5md libiompstubs5md pytorch_jni torch torch_cpu torch_global_deps)
