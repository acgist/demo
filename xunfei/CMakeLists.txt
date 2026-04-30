cmake_minimum_required(VERSION 3.22.0)

project(xunfei-project VERSION 1.0.0 LANGUAGES C CXX)

add_executable(
    xtts
    xtts/main.cpp
)

target_link_libraries(
    xtts
    ${CMAKE_CURRENT_SOURCE_DIR}/xtts/AEE_lib.lib
)

add_executable(
    cnenesr
    cnenesr/main.cpp
    cnenesr/winrec.c
    cnenesr/speech_recognizer.cpp
)

target_link_libraries(
    cnenesr
    ${CMAKE_CURRENT_SOURCE_DIR}/cnenesr/AEE_lib.lib
)
