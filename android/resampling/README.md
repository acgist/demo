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
