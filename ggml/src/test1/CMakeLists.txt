add_executable(testLine TestLine.cpp)
add_test(NAME testLine COMMAND testLine)
target_link_libraries(
    testLine PUBLIC
    ggml
)
set_target_properties(testLine PROPERTIES FOLDER "test1")

add_executable(testMatplot TestMatplot.cpp)
add_test(NAME testMatplot COMMAND testMatplot)
target_link_libraries(
  testMatplot PUBLIC
  Matplot++::matplot
)
set_target_properties(testMatplot PROPERTIES FOLDER "test1")
