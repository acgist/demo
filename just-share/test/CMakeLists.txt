add_executable(test_udp TestUdp.cpp)
add_test(NAME test_udp COMMAND test_udp)
set_target_properties(test_udp PROPERTIES FOLDER "test")
target_link_libraries(
    test_udp
    jsharecore
)

add_executable(test_tcp TestTcp.cpp)
add_test(NAME test_tcp COMMAND test_tcp)
set_target_properties(test_tcp PROPERTIES FOLDER "test")
target_link_libraries(
    test_tcp
    jsharecore
)
