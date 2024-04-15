#include "../include/RnnoiseProcesser.hpp"

namespace xinyuanhe {

RnnoiseConfig::RnnoiseConfig() {
}

RnnoiseConfig::~RnnoiseConfig() {
    delete[] this->rnnoiseData;
    rnnoise_destroy(this->denoiseState);
}

void RnnoiseConfig::init() {
    this->denoiseState = rnnoise_create(NULL);
    this->rnnoiseData = new float[this->rnnoiseSize];
}

}

extern "C" JNIEXPORT jlong JNICALL
Java_com_xinyuanhe_mediasoup_media_RnnoiseProcesser_Init(
    JNIEnv* env,
    jobject processer,
    jint bits,
    jint size,
    jint rate
) {
    __android_log_print(ANDROID_LOG_DEBUG, RNNOISE_TAG, "初始化Rnnoise");
    xinyuanhe::RnnoiseConfig* config = new xinyuanhe::RnnoiseConfig();
    config->bits          = bits;
    config->size          = size;
    config->rate          = rate;
    config->rnnoiseSize   = size / 2;
    config->init();
    return (jlong) config;
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_xinyuanhe_mediasoup_media_RnnoiseProcesser_Rnnoise(
    JNIEnv* env,
    jobject processer,
    jlong pointer,
    jbyteArray pcm
) {
    xinyuanhe::RnnoiseConfig* config = (xinyuanhe::RnnoiseConfig*) pointer;
    jbyte* srcBytes = env->GetByteArrayElements(pcm, 0);
    short* srcBuffer = (short*) srcBytes;
    for (int i = 0; i < config->rnnoiseSize; i++) {
        config->rnnoiseData[i] = srcBuffer[i];
    }
    rnnoise_process_frame(config->denoiseState, config->rnnoiseData, config->rnnoiseData);
    // 返回值不用释放否则需要手动释放
    const jbyteArray result = env->NewByteArray(config->size);
    jbyte dstBytes[config->size];
    for (int i = 0; i < config->rnnoiseSize; i++) {
        short v = config->rnnoiseData[i];
        if(v > std::numeric_limits<short>::max()) {
            v = std::numeric_limits<short>::max();
        } else if(v < std::numeric_limits<short>::min()) {
            v = std::numeric_limits<short>::min();
        }
        dstBytes[2 * i]     = (int8_t) (v >> 0);
        dstBytes[2 * i + 1] = (int8_t) (v >> 8);
    }
    env->SetByteArrayRegion(result, 0, config->size, dstBytes);
    env->ReleaseByteArrayElements(pcm, srcBytes, 0);
//    env->DeleteLocalRef(result);
//    env->ReleaseByteArrayElements(result, dstBytes, 0);
    return result;
}

extern "C" JNIEXPORT void JNICALL
Java_com_xinyuanhe_mediasoup_media_RnnoiseProcesser_Release(
    JNIEnv* env,
    jobject processer,
    jlong pointer
) {
    __android_log_print(ANDROID_LOG_DEBUG, RNNOISE_TAG, "释放资源Rnnoise");
    xinyuanhe::RnnoiseConfig* config = (xinyuanhe::RnnoiseConfig*) pointer;
    delete config;
}