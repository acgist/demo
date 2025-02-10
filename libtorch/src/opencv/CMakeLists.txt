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

add_executable(testMask TestMask.cpp)
add_test(NAME testMask COMMAND testMask)
target_link_libraries(
    testMask PUBLIC
    ${OpenCV_LIBS}
)
SET_TARGET_PROPERTIES(testMask PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

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