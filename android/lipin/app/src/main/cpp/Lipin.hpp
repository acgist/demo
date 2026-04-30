#pragma once

#include <map>

#include "jni.h"
#include "android/log.h"

#include "rnnoise.h"

extern "C" {
#include "libavutil/opt.h"
#include "libavutil/samplefmt.h"
#include "libavutil/channel_layout.h"
#include "libswresample/swresample.h"
}

#define TAG "lipin"

#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,  TAG, __VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,  TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
