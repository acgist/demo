add_executable(testXT TestXT.cpp)
add_test(NAME testXT COMMAND testXT)
target_link_libraries(
    testXT PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testXT PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testCVT TestCVT.cpp)
add_test(NAME testCVT COMMAND testCVT)
target_link_libraries(
    testCVT PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testCVT PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testOPT TestOPT.cpp)
add_test(NAME testOPT COMMAND testOPT)
target_link_libraries(
    testOPT PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testOPT PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testDraw TestDraw.cpp)
add_test(NAME testDraw COMMAND testDraw)
target_link_libraries(
    testDraw PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testDraw PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testHIST TestHIST.cpp)
add_test(NAME testHIST COMMAND testHIST)
target_link_libraries(
    testHIST PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testHIST PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testWarp TestWarp.cpp)
add_test(NAME testWarp COMMAND testWarp)
target_link_libraries(
    testWarp PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testWarp PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testEdge TestEdge.cpp)
add_test(NAME testEdge COMMAND testEdge)
target_link_libraries(
    testEdge PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testEdge PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testMask TestMask.cpp)
add_test(NAME testMask COMMAND testMask)
target_link_libraries(
    testMask PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testMask PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testFilter TestFilter.cpp)
add_test(NAME testFilter COMMAND testFilter)
target_link_libraries(
    testFilter PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testFilter PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testConv2dEx TestConv2d.cpp)
add_test(NAME testConv2dEx COMMAND testConv2dEx)
target_link_libraries(
    testConv2dEx PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testConv2dEx PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testBackProject TestBackProject.cpp)
add_test(NAME testBackProject COMMAND testBackProject)
target_link_libraries(
    testBackProject PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testBackProject PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testImagePool TestImagePool.cpp)
add_test(NAME testImagePool COMMAND testImagePool)
target_link_libraries(
    testImagePool PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testImagePool PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testAudio TestAudio.cpp)
add_test(NAME testAudio COMMAND testAudio)
target_link_libraries(
    testAudio PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testAudio PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")