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

```
cmake_minimum_required(VERSION 3.21.0)

project(lifuren VERSION 1.0.0 LANGUAGES C CXX)

#  -D CMAKE_BUILD_TYPE=Debug|Release
# SET(CMAKE_BUILD_TYPE Debug)
# SET(CMAKE_BUILD_TYPE Release)

# https://cmake.org/cmake/help/latest/prop_tgt/C_STANDARD.html
# https://cmake.org/cmake/help/latest/prop_tgt/CXX_STANDARD.html
set(CMAKE_C_STANDARD   17)
set(CMAKE_CXX_STANDARD 17)

if(CMAKE_HOST_UNIX)
    set(CMAKE_C_FLAGS           "-std=c17 -Wall -Wextra -Wno-unused-variable -Wno-unused-parameter")
    set(CMAKE_C_FLAGS_DEBUG     "${CMAKE_C_FLAGS_DEBUG} -Wunused-variable -Wunused-parameter -std=c17 -O0 -g")
    set(CMAKE_C_FLAGS_RELEASE   "${CMAKE_C_FLAGS_RELEASE} -Wunused-variable -Wunused-parameter -std=c17 -O3")
    set(CMAKE_CXX_FLAGS         "-std=c++17 -Wall -Wextra -Wno-unused-variable -Wno-unused-parameter")
    set(CMAKE_CXX_FLAGS_DEBUG   "${CMAKE_CXX_FLAGS_DEBUG} -Wunused-variable -Wunused-parameter -std=c++17 -O0 -g")
    set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wunused-variable -Wunused-parameter -std=c++17 -O3")
elseif(CMAKE_HOST_WIN32)
    if(MSVC)
        # /wd4804 = 忽略告警
        # /wd4819 = 忽略告警
        set(CMAKE_C_FLAGS   "${CMAKE_C_FLAGS}   /wd4804 /wd4819")
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /wd4804 /wd4819")
        add_compile_options("/utf-8")
        add_compile_options("/Zc:__cplusplus")
    else()
        message("需要安装MSVC")
    endif()
else()
    message("不支持的系统：${CMAKE_HOST_SYSTEM_NAME}")
endif()

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/debug/lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/debug/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/debug/bin)

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/release/lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/release/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/release/bin)

set(HEADER_DIR src/header/)
set(SOURCE_DIR src/source/)

set(
    CMAKE_INSTALL_PREFIX
    ${CMAKE_SOURCE_DIR}/target/
)

set(
    CMAKE_INSTALL_INCLUDEDIR
    ${CMAKE_INSTALL_PREFIX}/include/
)

set(
    CMAKE_PREFIX_PATH
    ${CMAKE_PREFIX_PATH}
    deps/libtorch
)

include_directories(
    deps/libtorch/include
    deps/libtorch/include/torch/csrc/api/include
)

if(CMAKE_HOST_WIN32)
    set(
        CMAKE_PREFIX_PATH
        ${CMAKE_PREFIX_PATH}
        deps/fltk
        deps/mlpack
        deps/opencv
    )

    include_directories(
        deps/fltk/include
        deps/mlpack/include
        deps/opencv/include
    )

    set(FLTK_FLUID_EXECUTABLE deps/fltk/tools/fltk)
    
    if(CMAKE_BUILD_TYPE MATCHES "Debug")
        file(GLOB FLTK_DLLS   "deps/fltk/debug/bin/*.dll")
        file(GLOB MLPACK_DLLS "deps/mlpack/debug/bin/*.dll")
    else()
        file(GLOB FLTK_DLLS   "deps/fltk/bin/*.dll")
        file(GLOB MLPACK_DLLS "deps/mlpack/bin/*.dll")
    endif()
    file(GLOB TORCH_DLLS  "deps/libtorch/lib/*.dll")
    file(GLOB OPENCV_DLLS "deps/opencv/x64/vc16/bin/*.dll")
endif()

find_package(FLTK      REQUIRED)
find_package(Torch     REQUIRED)
find_package(OpenCV    REQUIRED)
find_package(LAPACK    REQUIRED)
find_package(Armadillo REQUIRED)

add_subdirectory(deps)
add_subdirectory(boot)
add_subdirectory(core)
add_subdirectory(launcher)

set(HELP "LICENSE" "README.md")

install(
    FILES
    ${HELP} DESTINATION ${CMAKE_INSTALL_PREFIX}/
)

install(DIRECTORY DESTINATION ${CMAKE_INSTALL_PREFIX}/data/)
install(DIRECTORY DESTINATION ${CMAKE_INSTALL_PREFIX}/logs/)
install(DIRECTORY DESTINATION ${CMAKE_INSTALL_PREFIX}/model/)
```

```
project(lifurentemplate VERSION 1.0.0 LANGUAGES C CXX)

aux_source_directory(${SOURCE_DIR} SOURCE_FILES)

add_executable(${PROJECT_NAME} src/Main.cpp ${SOURCE_FILES})

target_link_libraries(
    ${PROJECT_NAME} PUBLIC
    lifurenboot
)

target_include_directories(
    ${PROJECT_NAME} PUBLIC
    ${HEADER_DIR}
)

set(
    HEADER_FILES
    ${HEADER_DIR}/Template.hpp
)

install(
    FILES
    ${HEADER_FILES} DESTINATION ${CMAKE_INSTALL_INCLUDEDIR}/${PROJECT_NAME}
)

install(
    TARGETS ${PROJECT_NAME} EXPORT ${PROJECT_NAME}-targets
    RUNTIME DESTINATION ${CMAKE_INSTALL_PREFIX}/bin/
    LIBRARY DESTINATION ${CMAKE_INSTALL_PREFIX}/lib/
    ARCHIVE DESTINATION ${CMAKE_INSTALL_PREFIX}/lib/
    PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_PREFIX}/include/${PROJECT_NAME}
)
```