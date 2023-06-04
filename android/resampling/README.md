# 重采样和降噪

```
https://github.com/xiph/rnnoise/
https://ffmpeg.org/releases/ffmpeg-4.4.4.tar.gz
```

## SDK

```
sudo apt install android-sdk
sudo apt install android-tools-adb
sudo apt install android-tools-fastboot
```

## NDK

* https://developer.android.google.cn/ndk/downloads?hl=zh-cn

## 编译

* https://github.com/Javernaut/ffmpeg-android-maker
* https://dl.google.com/android/repository/android-ndk-r23c-linux.zip?hl=zh-cn

```
export ANDROID_SDK_HOME=/usr/lib/android-sdk
export ANDROID_NDK_HOME=/data/dev/android-ndk-r23c
./ffmpeg-android-maker.sh -abis=armeabi-v7a,arm64-v8a,x86,x86_64 --source-tar=4.4.4 -binutils=llvm --android-api-level=28
```

## 文件

```
链接: https://pan.baidu.com/s/1j_2Sinc4r7oFjNpwyiCMhw
密码: u3xr
```

## 依赖

```
libavutil.so
libswscale.so
libavcodec.so
libavdevice.so
libavformat.so
libavfilter.so
libswresample.so
```

> 重采样只需要：`libavutil.so`、`libswresample.so`

## rnnoise

```
cmake_minimum_required(VERSION 3.22.1)

project(rnnoise VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -std=c11 -O0 -g")
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -std=c11 -O3")

set(
    SOURCE_DIR
    src
)

set(
    SOURCE_FILES
    ${SOURCE_DIR}/rnn.h
    ${SOURCE_DIR}/arch.h
    ${SOURCE_DIR}/pitch.h
    ${SOURCE_DIR}/common.h
    ${SOURCE_DIR}/rnn_data.h
    ${SOURCE_DIR}/celt_lpc.h
    ${SOURCE_DIR}/kiss_fft.h
    ${SOURCE_DIR}/opus_types.h
    ${SOURCE_DIR}/tansig_table.h
    ${SOURCE_DIR}/_kiss_fft_guts.h
    ${SOURCE_DIR}/rnn.c
    ${SOURCE_DIR}/pitch.c
    ${SOURCE_DIR}/denoise.c
    ${SOURCE_DIR}/rnn_data.c
    ${SOURCE_DIR}/kiss_fft.c
    ${SOURCE_DIR}/celt_lpc.c
    ${SOURCE_DIR}/rnn_reader.c
)

include_directories(include)

add_library(${PROJECT_NAME} SHARED ${SOURCE_FILES})

set_source_files_properties(
    ${SOURCE_FILES} PROPERTIES COMPILE_FLAGS -Wall -Wextra -Wpedantic
)

target_include_directories(
    ${PROJECT_NAME} PUBLIC
    "${SOURCE_DIR}/include"
)
```