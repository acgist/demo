project(jsharecore VERSION 1.0.0 LANGUAGES C CXX)

find_package(Threads REQUIRED)

add_library(
    ${PROJECT_NAME}
    src/cli.cpp
    src/gui.cpp
    src/udp.cpp
    src/jshare.cpp
)

target_link_libraries(${PROJECT_NAME} PUBLIC Threads::Threads)

target_include_directories(${PROJECT_NAME} PUBLIC include)

install(
    TARGETS ${PROJECT_NAME} EXPORT ${PROJECT_NAME}-targets
    ARCHIVE DESTINATION ${CMAKE_INSTALL_PREFIX}/lib/
    LIBRARY DESTINATION ${CMAKE_INSTALL_PREFIX}/bin/
    RUNTIME DESTINATION ${CMAKE_INSTALL_PREFIX}/bin/
    PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_PREFIX}/include/
)
install(DIRECTORY include DESTINATION ${CMAKE_INSTALL_PREFIX}/include/)
