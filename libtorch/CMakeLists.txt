cmake_minimum_required(VERSION 3.20.0)

project(libtorch-project VERSION 1.0.0 LANGUAGES C CXX)

# -D CMAKE_BUILD_TYPE=Debug|Release
if(NOT CMAKE_BUILD_TYPE)
    SET(CMAKE_BUILD_TYPE Debug)
    # SET(CMAKE_BUILD_TYPE Release)
endif()

# https://cmake.org/cmake/help/latest/prop_tgt/C_STANDARD.html
# https://cmake.org/cmake/help/latest/prop_tgt/CXX_STANDARD.html
set(CMAKE_C_STANDARD   17)
set(CMAKE_CXX_STANDARD 20)

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
        deps/opencv
        deps/matplotplusplus
    )

    include_directories(
        deps/opencv/include
        deps/matplotplusplus/include
    )

    file(GLOB TORCH_DLLS  "deps/libtorch/lib/*.dll")
    file(GLOB OPENCV_DLLS "deps/opencv/x64/vc16/bin/opencv_videoio_ffmpeg4100_64.dll"
                          "deps/opencv/x64/vc16/bin/opencv_videoio_msmf4100_64d.dll"
                          "deps/opencv/x64/vc16/bin/opencv_world4100d.dll"
    )
endif()

find_package(Torch     REQUIRED)
find_package(OpenCV    REQUIRED)
find_package(Matplot++ REQUIRED)

add_executable(${PROJECT_NAME} src/source/Main.cpp)

add_executable(testCV src/source/TestCV.cpp)
add_test(NAME testCV COMMAND testCV)
target_link_libraries(
    testCV PUBLIC
    ${OpenCV_LIBS}
)

add_executable(testMLP src/source/Files.cpp src/source/Datasets.cpp src/source/TestMLP.cpp)
add_test(NAME testMLP COMMAND testMLP)
target_link_libraries(
    testMLP PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testLine src/source/TestLine.cpp)
add_test(NAME testLine COMMAND testLine)
target_link_libraries(
    testLine PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testConv2d src/source/TestConv2d.cpp)
add_test(NAME testConv2d COMMAND testConv2d)
target_link_libraries(
    testConv2d PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testGPU src/source/TestGPU.cpp)
add_test(NAME testGPU COMMAND testGPU)
target_link_libraries(
    testGPU PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testLayer src/source/TestLayer.cpp)
add_test(NAME testLayer COMMAND testLayer)
target_link_libraries(
    testLayer PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testMemory src/source/TestMemory.cpp)
add_test(NAME testMemory COMMAND testMemory)
target_link_libraries(
    testMemory PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testHouse src/source/Files.cpp src/source/Datasets.cpp src/source/TestHouse.cpp)
add_test(NAME testHouse COMMAND testHouse)
target_link_libraries(
    testHouse PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testDataset src/source/Files.cpp src/source/Datasets.cpp src/source/TestDataset.cpp)
add_test(NAME testDataset COMMAND testDataset)
target_link_libraries(
    testDataset PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testMlatplot src/source/TestMatplot.cpp)
add_test(NAME testMlatplot COMMAND testMlatplot)
target_link_libraries(
    testMlatplot PUBLIC
    Matplot++::matplot
)

add_executable(testTensor src/source/TestTensor.cpp)
add_test(NAME testTensor COMMAND testTensor)
target_link_libraries(
    testTensor PUBLIC
    ${OpenCV_LIBS}
    ${TORCH_LIBRARIES}
)

add_executable(testReg src/source/TestReg.cpp)
add_test(NAME testReg COMMAND testReg)
target_link_libraries(
    testReg PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testLoss src/source/TestLoss.cpp)
add_test(NAME testLoss COMMAND testLoss)
target_link_libraries(
    testLoss PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testClassify src/source/Files.cpp src/source/Datasets.cpp  src/source/TestClassify.cpp)
add_test(NAME testClassify COMMAND testClassify)
target_link_libraries(
    testClassify PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testMultiLine src/source/Files.cpp src/source/Datasets.cpp src/source/TestMultiLine.cpp)
add_test(NAME testMultiLine COMMAND testMultiLine)
target_link_libraries(
    testMultiLine PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testMultiLineX src/source/Files.cpp src/source/Datasets.cpp src/source/TestMultiLineX.cpp)
add_test(NAME testMultiLineX COMMAND testMultiLineX)
target_link_libraries(
    testMultiLineX PUBLIC
    Matplot++::matplot
    ${TORCH_LIBRARIES}
)

add_executable(testLinear src/source/Files.cpp src/source/Datasets.cpp src/source/TestLinear.cpp)
add_test(NAME testLinear COMMAND testLinear)
target_link_libraries(
    testLinear PUBLIC
    Matplot++::matplot
    ${TORCH_LIBRARIES}
)

add_executable(testLibTorchLine src/source/Files.cpp src/source/Datasets.cpp src/source/TestLibTorchLine.cpp)
add_test(NAME testLibTorchLine COMMAND testLibTorchLine)
target_link_libraries(
    testLibTorchLine PUBLIC
    Matplot++::matplot
    ${TORCH_LIBRARIES}
)

add_executable(testQuantitative src/source/TestQuantitative.cpp)
add_test(NAME testQuantitative COMMAND testQuantitative)
target_link_libraries(
    testQuantitative PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testGenderHandler src/source/Files.cpp src/source/Layers.cpp src/source/Datasets.cpp src/source/TestGenderHandler.cpp)
add_test(NAME testGenderHandler COMMAND testGenderHandler)
target_link_libraries(
  testGenderHandler PUBLIC
    ${OpenCV_LIBS}
    ${TORCH_LIBRARIES}
)

add_executable(testFace src/opencv/TestFace.cpp)
add_test(NAME testFace COMMAND testFace)
target_link_libraries(
    testFace PUBLIC
    ${OpenCV_LIBS}
)

add_executable(testColor src/opencv/TestColor.cpp)
add_test(NAME testColor COMMAND testColor)
target_link_libraries(
    testColor PUBLIC
    ${OpenCV_LIBS}
)

add_executable(testResize src/opencv/TestResize.cpp)
add_test(NAME testResize COMMAND testResize)
target_link_libraries(
    testResize PUBLIC
    ${OpenCV_LIBS}
)

add_executable(testModel src/libtorch/TestModel.cpp)
add_test(NAME testModel COMMAND testModel)
target_link_libraries(
    testModel PUBLIC
    ${TORCH_LIBRARIES}
)

add_executable(testLibTorchTensor src/libtorch/TestTensor.cpp)
add_test(NAME testLibTorchTensor COMMAND testLibTorchTensor)
target_link_libraries(
    testLibTorchTensor PUBLIC
    ${OpenCV_LIBS}
    ${TORCH_LIBRARIES}
)

add_subdirectory(src/opencv)

add_custom_command(
    TARGET ${PROJECT_NAME} POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different ${TORCH_DLLS}  $<TARGET_FILE_DIR:${PROJECT_NAME}>
    COMMAND ${CMAKE_COMMAND} -E copy_if_different ${OPENCV_DLLS} $<TARGET_FILE_DIR:${PROJECT_NAME}>
)
