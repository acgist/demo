cmake_minimum_required(VERSION 3.22.0)

project(cert VERSION 1.0.0 LANGUAGES C CXX)

if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Release)
endif()

set_property(GLOBAL PROPERTY USE_FOLDERS ON)

if(CMAKE_HOST_WIN32)
    if(MSVC)
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /std:c++20")
        set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} /wd4244 /wd4267")
        
        add_compile_options("/utf-8")
        add_compile_options("/Zc:__cplusplus")
    else()
        message(FATAL_ERROR "需要安装MSVC")
    endif()
else()
    message(FATAL_ERROR "不支持的系统：${CMAKE_HOST_SYSTEM_NAME}")
endif()

message(STATUS "CXX_FLAGS: ${CMAKE_CXX_FLAGS}")
message(STATUS "CXX_FLAGS_DEBUG: ${CMAKE_CXX_FLAGS_DEBUG}")
message(STATUS "CXX_FLAGS_RELEASE: ${CMAKE_CXX_FLAGS_RELEASE}")
message(STATUS "CMAKE_INSTALL_PREFIX: ${CMAKE_INSTALL_PREFIX}")

add_executable(
    ${PROJECT_NAME}
    main.cpp
    win.rc
)

if(CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(CMAKE_EXE_LINKER_FLAGS "/MANIFESTUAC:\"level='highestAvailable' uiAccess='false'\" /NODEFAULTLIB:MSVCRT")

    target_link_options(${PROJECT_NAME} PRIVATE "/SUBSYSTEM:CONSOLE")
else()
    set(CMAKE_EXE_LINKER_FLAGS "/MANIFESTUAC:\"level='highestAvailable' uiAccess='false'\" /NODEFAULTLIB:MSVCRTD /entry:mainCRTStartup")

    target_link_options(${PROJECT_NAME} PRIVATE "/SUBSYSTEM:WINDOWS")
endif()
