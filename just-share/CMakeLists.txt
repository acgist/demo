cmake_minimum_required(VERSION 3.22.0)

project(jshare-project VERSION 1.0.0 LANGUAGES C CXX)

if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Debug)
endif()

set_property(GLOBAL PROPERTY USE_FOLDERS ON)

if(CMAKE_HOST_WIN32)
    if(MSVC)
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /std:c++20")
        add_compile_options("/utf-8")
        add_compile_options("/Zc:__cplusplus")
    else()
        message(FATAL_ERROR "需要安装MSVC")
    endif()
elseif(CMAKE_HOST_UNIX)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++20 -Wall -Wextra")
    set_property(GLOBAL PROPERTY RULE_LAUNCH_LINK    "${CMAKE_COMMAND} -E time")
    set_property(GLOBAL PROPERTY RULE_LAUNCH_COMPILE "${CMAKE_COMMAND} -E time")
else()
    message(FATAL_ERROR "不支持的系统：${CMAKE_HOST_SYSTEM_NAME}")
endif()

find_package(Threads REQUIRED)

add_library(
    jsharecore
    src/udp.cpp
    src/tcp.cpp
)

target_link_libraries(jsharecore PUBLIC Threads::Threads)

target_include_directories(jsharecore PUBLIC include)

if(CMAKE_HOST_WIN32)
    add_executable(
        jshare
        src/win.rc
        src/main.cpp
    )
else()
    add_executable(
        jshare
        src/main.cpp
    )
endif()

target_link_libraries(jshare jsharecore)

target_include_directories(jshare PUBLIC include)

if(CMAKE_HOST_WIN32)
    if(CMAKE_BUILD_TYPE STREQUAL "Debug")
        set(CMAKE_EXE_LINKER_FLAGS "/NODEFAULTLIB:MSVCRT")
    else()
        set(CMAKE_EXE_LINKER_FLAGS "/NODEFAULTLIB:MSVCRTD")
    endif()
endif()

add_subdirectory(test)
