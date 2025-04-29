cmake_minimum_required(VERSION 3.22.0)

if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Debug)
endif()

project(lifuren-project VERSION 1.0.0 LANGUAGES C CXX)

set(HEADER_DIR include/)

set_property(GLOBAL PROPERTY USE_FOLDERS ON)

if(MSVC)
    set(CMAKE_C_FLAGS   "${CMAKE_C_FLAGS} /std:c17")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /std:c++20")
    set(CMAKE_C_FLAGS_DEBUG   "${CMAKE_C_FLAGS_DEBUG} /wd4804 /wd4819")
    set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} /wd4804 /wd4819")
    add_compile_options("/utf-8")
    add_compile_options("/Zc:__cplusplus")
else()
    message(FATAL_ERROR "需要安装MSVC")
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

set(
    CMAKE_PREFIX_PATH
    ${CMAKE_PREFIX_PATH}
    deps/opencv
    deps/matplot
    deps/libtorch
)

file(GLOB TORCH_DLLS  "deps/libtorch/lib/*.dll")
file(GLOB OPENCV_DLLS "deps/opencv/x64/vc16/bin/*.dll")

find_package(Torch     CONFIG REQUIRED)
find_package(OpenCV    CONFIG REQUIRED)
find_package(Matplot++ CONFIG REQUIRED)

add_executable(testGender TestGender.cpp)
add_test(NAME testGender COMMAND testGender)
target_link_libraries(
    testGender
    ${OpenCV_LIBS}
    ${TORCH_LIBRARIES}
)

add_executable(testLinear TestLinear.cpp)
add_test(NAME testLinear COMMAND testLinear)
target_link_libraries(
    testLinear
    Matplot++::matplot
    ${OpenCV_LIBS}
    ${TORCH_LIBRARIES}
)

add_executable(testClassify TestClassify.cpp)
add_test(NAME testClassify COMMAND testClassify)
target_link_libraries(
    testClassify
    Matplot++::matplot
    ${OpenCV_LIBS}
    ${TORCH_LIBRARIES}
)

add_custom_command(
    TARGET testLinear POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different ${TORCH_DLLS}  ${CMAKE_LIBRARY_OUTPUT_DIRECTORY}
    COMMAND ${CMAKE_COMMAND} -E copy_if_different ${OPENCV_DLLS} ${CMAKE_LIBRARY_OUTPUT_DIRECTORY}
)
