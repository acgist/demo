# CMakeLists编写

https://zhuanlan.zhihu.com/p/473573789

```
# 指定最小版本
cmake_minimum_required(VERSION 3.4.1)

# 设置项目名称
project(demo)

add_executable(demo demo.cpp)       # 生成可执行文件
add_library(common STATIC utils.cpp) # 生成静态库
add_library(common SHARED utils.cpp) # 生成动态库或共享库

# 指定包含文件
add_library(demo demo.cpp test.cpp util.cpp)

# 搜索cpp文件
aux_source_directory(. SRC_LIST)
add_library(demo ${SRC_LIST})

# 自定义搜索
file(GLOB SRC_LIST "*.cpp" "protocol/*.cpp")
add_library(demo ${SRC_LIST})
# 或者
file(GLOB SRC_LIST "*.cpp")
file(GLOB SRC_PROTOCOL_LIST "protocol/*.cpp")
add_library(demo ${SRC_LIST} ${SRC_PROTOCOL_LIST})
# 或者
file(GLOB_RECURSE SRC_LIST "*.cpp")
file(GLOB SRC_PROTOCOL RELATIVE "protocol" "*.cpp")
add_library(demo ${SRC_LIST} ${SRC_PROTOCOL_LIST})
# 或者
aux_source_directory(. SRC_LIST)
aux_source_directory(protocol SRC_PROTOCOL_LIST)
add_library(demo ${SRC_LIST} ${SRC_PROTOCOL_LIST})

# 查找指定的库文件
find_library(
    log-lib
    log
)

# 设置包含的目录
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_BINARY_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/include
)

# 设置链接库搜索目录
link_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}/libs
)

# 指定链接动态库或静态库
target_link_libraries(demo libface.a) # 链接libface.a
target_link_libraries(demo libface.so) # 链接libface.so
```

