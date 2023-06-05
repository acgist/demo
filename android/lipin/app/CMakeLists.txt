cmake_minimum_required(VERSION 3.22.1)

project(lipin VERSION 1.0.0 LANGUAGES C CXX)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -std=c11 -O0 -g")
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -std=c11 -O3")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -std=c++11 -O0 -g")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -std=c++11 -O3")

set(
    SOURCE_DIR
    src/main/cpp/
)

set(
    SOURCE_FILES
    ${SOURCE_DIR}/Lipin.hpp
    ${SOURCE_DIR}/Lipin.cpp
)

add_library(${PROJECT_NAME} SHARED ${SOURCE_FILES})

add_subdirectory("deps/rnnoise")

set_source_files_properties(
    ${SOURCE_FILES} PROPERTIES COMPILE_FLAGS -Wall -Wextra -Wpedantic
)

target_include_directories(
    ${PROJECT_NAME} PUBLIC
    "${PROJECT_SOURCE_DIR}/deps/ffmpeg/include"
    "${PROJECT_SOURCE_DIR}/deps/rnnoise/include"
)

add_library(
    avutil
    SHARED
    IMPORTED
)
set_target_properties(
    avutil
    PROPERTIES IMPORTED_LOCATION
    ../../../../libs/${ANDROID_ABI}/libavutil.so
)

add_library(
    swresample
    SHARED
    IMPORTED
)
set_target_properties(
    swresample
    PROPERTIES IMPORTED_LOCATION
    ../../../../libs/${ANDROID_ABI}/libswresample.so
)

target_link_libraries(
    ${PROJECT_NAME} PUBLIC
    log
    avutil
    android
    rnnoise
    swresample
)
