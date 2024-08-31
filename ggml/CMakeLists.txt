cmake_minimum_required(VERSION 3.20.0)

project(ggml-project VERSION 1.0.0 LANGUAGES C CXX)

# -D CMAKE_BUILD_TYPE=Debug|Release
if(NOT CMAKE_BUILD_TYPE)
    SET(CMAKE_BUILD_TYPE Debug)
    # SET(CMAKE_BUILD_TYPE Release)
endif()

# https://cmake.org/cmake/help/latest/prop_tgt/C_STANDARD.html
# https://cmake.org/cmake/help/latest/prop_tgt/CXX_STANDARD.html
set(CMAKE_C_STANDARD   17)
set(CMAKE_CXX_STANDARD 20)

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR})
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR})

if(CMAKE_HOST_UNIX)
    # https://gcc.gnu.org/onlinedocs/gcc/Option-Summary.html
    set(CMAKE_C_FLAGS           "${CMAKE_C_FLAGS} -std=c17 -Wall -Wextra")
    set(CMAKE_C_FLAGS_DEBUG     "${CMAKE_C_FLAGS_DEBUG}   -Wno-unused-variable -Wno-unused-parameter -O0 -g")
    set(CMAKE_C_FLAGS_RELEASE   "${CMAKE_C_FLAGS_RELEASE} -Wunused-variable    -Wunused-parameter    -O3")
    set(CMAKE_CXX_FLAGS         "${CMAKE_CXX_FLAGS} -std=c++20 -Wall -Wextra")
    set(CMAKE_CXX_FLAGS_DEBUG   "${CMAKE_CXX_FLAGS_DEBUG}   -Wno-unused-variable -Wno-unused-parameter -O0 -g")
    set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wunused-variable    -Wunused-parameter    -O3")
elseif(CMAKE_HOST_WIN32)
    if(MSVC)
        # https://learn.microsoft.com/zh-cn/cpp/build/reference/compiler-options-listed-by-category
        # /wd4804 = 忽略告警
        # /wd4819 = 忽略告警
        set(CMAKE_C_FLAGS   "${CMAKE_C_FLAGS}   /std:c17 /std:c++20 /wd4804 /wd4819")
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /std:c17 /std:c++20 /wd4804 /wd4819")
        # add_compile_options("/J")
        add_compile_options("/utf-8")
        # add_compile_options("/Zc:char8_t")
        add_compile_options("/Zc:__cplusplus")
    else()
        message(WARNING "需要安装MSVC")
    endif()
else()
    message(FATAL_ERROR "不支持的系统：${CMAKE_HOST_SYSTEM_NAME}")
endif()

set(HEADER_DIR src/header/)
set(SOURCE_DIR src/source/)

if(CMAKE_HOST_WIN32)
    set(
        CMAKE_PREFIX_PATH
        ${CMAKE_PREFIX_PATH}
        deps/ggml
        deps/opencv
        deps/matplotplusplus
    )

    include_directories(
        deps/ggml/include
        deps/opencv/include
        deps/matplotplusplus/include
    )

    file(GLOB OPENCV_DLLS "deps/opencv/x64/vc16/bin/opencv_videoio_ffmpeg4100_64.dll"
                          "deps/opencv/x64/vc16/bin/opencv_videoio_msmf4100_64d.dll"
                          "deps/opencv/x64/vc16/bin/opencv_world4100d.dll"
    )
endif()

add_subdirectory(deps/ggml)

find_package(OpenCV    REQUIRED)
find_package(Matplot++ REQUIRED)

add_executable(${PROJECT_NAME} src/source/Main.cpp)

add_custom_command(
    TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different ${OPENCV_DLLS} $<TARGET_FILE_DIR:${PROJECT_NAME}>
)

add_executable(testLine src/source/TestLine.cpp)
add_test(NAME testLine COMMAND testLine)
target_link_libraries(
    testLine PUBLIC
    ggml
)