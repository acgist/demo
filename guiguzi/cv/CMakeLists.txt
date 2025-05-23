add_executable(
    aliang
    src/aliang/Main.cpp
    src/aliang/Aliang.cpp
    src/aliang/Detection.cpp
    src/aliang/Recognition.cpp
    src/aliang/OnnxRuntime.cpp
)
add_test(NAME aliang COMMAND aliang)
set_target_properties(aliang PROPERTIES FOLDER "cv")
target_link_libraries(
    aliang
    guiguziboot
)

add_executable(
    testHelmet
    src/helmet/Main.cpp
)
add_test(NAME testHelmet COMMAND testHelmet)
set_target_properties(testHelmet PROPERTIES FOLDER "cv")
target_link_libraries(
    testHelmet
    guiguziboot
)

add_executable(
    testSafebelt
    src/safebelt/Main.cpp
)
add_test(NAME testSafebelt COMMAND testSafebelt)
set_target_properties(testSafebelt PROPERTIES FOLDER "cv")
target_link_libraries(
    testSafebelt
    guiguziboot
)

add_executable(
    testOpenCV
    src/opencv/Main.cpp
)
add_test(NAME testOpenCV COMMAND testOpenCV)
set_target_properties(testOpenCV PROPERTIES FOLDER "cv")
target_link_libraries(
    testOpenCV
    guiguziboot
)

add_executable(
    testRange
    src/opencv/Range.cpp
)
add_test(NAME testRange COMMAND testRange)
set_target_properties(testRange PROPERTIES FOLDER "cv")
target_link_libraries(
    testRange
    guiguziboot
)

add_executable(
    testVideoCapture
    src/video-capture/Main.cpp
)
add_test(NAME testVideoCapture COMMAND testVideoCapture)
set_target_properties(testVideoCapture PROPERTIES FOLDER "cv")
target_link_libraries(
    testVideoCapture
    guiguziboot
)

add_executable(
    testFaceDetection
    src/face-detection/Main.cpp
    src/face-detection/FDOnnx.cpp
    src/face-detection/FDOpenCV.cpp
    src/face-detection/FDLibTorch.cpp
)
add_test(NAME testFaceDetection COMMAND testFaceDetection)
set_target_properties(testFaceDetection PROPERTIES FOLDER "cv")
target_link_libraries(
    testFaceDetection
    guiguziboot
)

add_executable(
    testFaceRecognition
    src/face-recognition/Main.cpp
    src/face-recognition/FROnnx.cpp
    src/face-recognition/FROpenCV.cpp
    src/face-recognition/FRFaceFeature.cpp
)
add_test(NAME testFaceRecognition COMMAND testFaceRecognition)
set_target_properties(testFaceRecognition PROPERTIES FOLDER "cv")
target_link_libraries(
    testFaceRecognition
    guiguziboot
)

add_subdirectory(src/track)
add_subdirectory(src/track/bytetracker)
