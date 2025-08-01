cmake_minimum_required(VERSION 3.22.0)

project(jshare-project VERSION 1.0.0 LANGUAGES C CXX)

if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Debug)
endif()

message(STATUS "当前模式: ${CMAKE_BUILD_TYPE}")
message(STATUS "当前编译器: ${CMAKE_CXX_COMPILER_ID}")

set_property(GLOBAL PROPERTY USE_FOLDERS ON)

if(CMAKE_HOST_WIN32)
    if(MSVC)
        # set(CMAKE_CXX_STANDARD 20)
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /std:c++20")
        set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} /wd4804 /wd4819")
        add_compile_options("/utf-8")
        add_compile_options("/Zc:__cplusplus")
    else()
        message(FATAL_ERROR "需要安装MSVC")
    endif()
elseif(CMAKE_HOST_UNIX)
    # set(CMAKE_CXX_STANDARD 20)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++20 -Wall -Wextra")
    set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Wno-unused-variable -Wno-unused-parameter -Wno-missing-field-initializers -O0 -g")
    set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -O3")
    set_property(GLOBAL PROPERTY RULE_LAUNCH_LINK    "${CMAKE_COMMAND} -E time")
    set_property(GLOBAL PROPERTY RULE_LAUNCH_COMPILE "${CMAKE_COMMAND} -E time")
else()
    message(FATAL_ERROR "不支持的系统：${CMAKE_HOST_SYSTEM_NAME}")
endif()

if(CMAKE_BUILD_TYPE STREQUAL "Debug")
    message(STATUS "CXX_FLAGS: ${CMAKE_CXX_FLAGS} ${CMAKE_CXX_FLAGS_DEBUG}")

    set(CMAKE_INSTALL_PREFIX ${CMAKE_SOURCE_DIR}/install)
else()
    message(STATUS "CXX_FLAGS: ${CMAKE_CXX_FLAGS} ${CMAKE_CXX_FLAGS_RELEASE}")
endif()

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/Debug/lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/Debug/bin)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/Debug/bin)

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/Release/lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/Release/bin)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/Release/bin)

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/${CMAKE_BUILD_TYPE}/lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/${CMAKE_BUILD_TYPE}/bin)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/${CMAKE_BUILD_TYPE}/bin)

add_subdirectory(core)
add_subdirectory(client)

set(INSTALL_DOCUMENT "LICENSE" "README.md")

install(FILES ${INSTALL_DOCUMENT} DESTINATION ${CMAKE_INSTALL_PREFIX}/)

file(COPY ${INSTALL_DOCUMENT} DESTINATION ${CMAKE_BINARY_DIR}/${CMAKE_BUILD_TYPE}/)
