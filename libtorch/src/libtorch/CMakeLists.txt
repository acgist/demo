add_executable(testPermute TestPermute.cpp)
add_test(NAME testPermute COMMAND testPermute)
target_link_libraries(
    testPermute PUBLIC
    ${TORCH_LIBRARIES}
)
SET_TARGET_PROPERTIES(testPermute PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")

add_executable(testSlowFast TestSlowFast.cpp)
add_test(NAME testSlowFast COMMAND testSlowFast)
target_link_libraries(
    testSlowFast PUBLIC
    ${OpenCV_LIBS}
    ${TORCH_LIBRARIES}
)
SET_TARGET_PROPERTIES(testSlowFast PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${PROJECT_BINARY_DIR}")
