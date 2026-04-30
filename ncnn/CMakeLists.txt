cmake_minimum_required(VERSION 3.20.0)

project(ncnn-runner VERSION 1.0.0 LANGUAGES C CXX)

if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Debug)
    # set(CMAKE_BUILD_TYPE Release)
endif()

if(CMAKE_HOST_UNIX)
    set(CMAKE_CXX_FLAGS         "${CMAKE_CXX_FLAGS} -std=c++20 -Wall -Wextra")
    set(CMAKE_CXX_FLAGS_DEBUG   "${CMAKE_CXX_FLAGS_DEBUG}   -Wno-unused-variable -Wno-unused-parameter -O0 -g")
    set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wunused-variable    -Wunused-parameter    -O3")
elseif(CMAKE_HOST_WIN32)
    if(MSVC)
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /std:c++20 /wd4804 /wd4819")
        add_compile_options("/utf-8")
        add_compile_options("/Zc:__cplusplus")
    else()
        message(WARNING "需要安装MSVC")
    endif()
else()
    message(FATAL_ERROR "不支持的系统：${CMAKE_HOST_SYSTEM_NAME}")
endif()

if(CMAKE_HOST_WIN32)
    set(
        CMAKE_PREFIX_PATH
        ${CMAKE_PREFIX_PATH}
        deps/ncnn
        deps/opencv
    )

    include_directories(
        deps/ncnn/include
        deps/opencv/include
    )

    file(GLOB NCNN_DLLS   "deps/ncnn/bin/*.dll")
    file(GLOB OPENCV_DLLS "deps/opencv/debug/bin/*.dll")
endif()

find_package(ncnn   REQUIRED)
find_package(OpenCV REQUIRED)

add_executable(${PROJECT_NAME} src/MainYolo11.cpp)
add_test(NAME ${PROJECT_NAME} COMMAND ${PROJECT_NAME})
target_link_libraries(
    ${PROJECT_NAME} PUBLIC
    ncnn
    ${OpenCV_LIBS}
)

add_custom_command(
    TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different ${NCNN_DLLS}   $<TARGET_FILE_DIR:${PROJECT_NAME}>
    COMMAND ${CMAKE_COMMAND} -E copy_if_different ${OPENCV_DLLS} $<TARGET_FILE_DIR:${PROJECT_NAME}>
)
