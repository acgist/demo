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