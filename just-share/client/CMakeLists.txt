if(CMAKE_HOST_APPLE)
    add_subdirectory(mac)
elseif(CMAKE_HOST_UNIX)
    add_subdirectory(linux)
elseif(CMAKE_HOST_WIN32)
    add_subdirectory(windows)
endif()

add_subdirectory(test)
