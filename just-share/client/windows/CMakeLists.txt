project(jshare VERSION 1.0.0 LANGUAGES C CXX)

add_executable(
    ${PROJECT_NAME}
    src/win.rc
    src/main.cpp
)
if(CMAKE_BUILD_TYPE STREQUAL "Debug")
    target_link_options(${PROJECT_NAME} PRIVATE "/SUBSYSTEM:CONSOLE")
else()
    target_link_options(${PROJECT_NAME} PRIVATE "/SUBSYSTEM:WINDOWS")
endif()

target_link_libraries(
    ${PROJECT_NAME}
    jsharecore
)

target_include_directories(${PROJECT_NAME} PUBLIC include)

install(
    TARGETS ${PROJECT_NAME} EXPORT ${PROJECT_NAME}-targets
    ARCHIVE DESTINATION ${CMAKE_INSTALL_PREFIX}/lib/
    LIBRARY DESTINATION ${CMAKE_INSTALL_PREFIX}/bin/
    RUNTIME DESTINATION ${CMAKE_INSTALL_PREFIX}/bin/
    PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_PREFIX}/include/
)
install(DIRECTORY include DESTINATION ${CMAKE_INSTALL_PREFIX}/include/)

if(CMAKE_HOST_WIN32)
    if(CMAKE_BUILD_TYPE STREQUAL "Debug")
        set(CMAKE_EXE_LINKER_FLAGS "/NODEFAULTLIB:MSVCRT")
    else()
        set(CMAKE_EXE_LINKER_FLAGS "/NODEFAULTLIB:MSVCRTD /entry:mainCRTStartup")
    endif()
endif()
