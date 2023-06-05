#include "Lipin.hpp"

extern "C" {

/**
 * 李凭配置
 */
class LipinConfig {

public:
    // 降噪状态
    DenoiseState* denoise_state;
    float* rnnoiseSrc;
    float* rnnoiseDst;
    // 重采样上下文
    SwrContext* swr_context;
    SwrContext* swr_context_restore;
    // 位深
    int bits = 16;
    uint8_t* srcBuffer;
    uint8_t* dstBuffer;
    // 原始数据配置
    int64_t src_ch_layout = AV_CH_LAYOUT_STEREO;
    int src_rate          = 8000;
    int src_linesize      = 0;
    int src_nb_samples    = 0;
    int srcBytesLength    = 0;
    int src_nb_channels   = av_get_channel_layout_nb_channels(this->src_ch_layout);
    AVSampleFormat src_sample_fmt = AV_SAMPLE_FMT_S16;
    // 目标数据配置
    int64_t dst_ch_layout = AV_CH_LAYOUT_STEREO;
    int dst_rate          = 48000;
    int dst_linesize      = 0;
    int dst_nb_samples    = 0;
    int dstBytesLength    = 0;
    int dst_nb_channels   = av_get_channel_layout_nb_channels(this->dst_ch_layout);
    AVSampleFormat dst_sample_fmt = AV_SAMPLE_FMT_S16;

public:
    LipinConfig() {
    }

    virtual ~LipinConfig() {
        delete this->denoise_state;
        delete this->srcBuffer;
        delete this->dstBuffer;
        delete this->rnnoiseSrc;
        delete this->rnnoiseDst;
        swr_free(&this->swr_context);
        swr_free(&this->swr_context_restore);
    }

public:
    /**
     * 加载配置
     */
    void init() {
        // 配置降噪
        this->denoise_state = rnnoise_create(NULL);
        // 配置重采样
        this->swr_context = swr_alloc();
        swr_alloc_set_opts(
            this->swr_context,
            // 输出通道
            this->dst_ch_layout,
            this->dst_sample_fmt,
            this->dst_rate,
            // 输入通道
            this->src_ch_layout,
            this->src_sample_fmt,
            this->src_rate,
            0,
            nullptr
        );
        swr_init(this->swr_context);
        // 配置重采样还原
        this->swr_context_restore = swr_alloc();
        swr_alloc_set_opts(
            this->swr_context_restore,
            // 输入通道
            this->src_ch_layout,
            this->src_sample_fmt,
            this->src_rate,
            // 输出通道
            this->dst_ch_layout,
            this->dst_sample_fmt,
            this->dst_rate,
            0,
            nullptr
        );
        swr_init(this->swr_context_restore);
        // 配置缓冲区
        this->src_nb_samples = srcBytesLength * 8 / this->src_nb_channels / this->bits;
        this->dst_nb_samples = av_rescale_rnd(src_nb_samples, this->dst_rate, this->src_rate, AV_ROUND_UP);
        this->dstBytesLength = dst_nb_samples * this->dst_nb_channels * this->bits / 8;
        this->srcBuffer      = new uint8_t[this->srcBytesLength];
        this->dstBuffer      = new uint8_t[this->dstBytesLength];
        this->rnnoiseSrc     = new float[this->dstBytesLength / 2];
        this->rnnoiseDst     = new float[this->dstBytesLength / 2];
    }

};

JNIEXPORT jlong JNICALL
Java_com_acgist_lipin_Lipin_Init(
    JNIEnv* env,
    jobject lipin,
    jint bits,
    jint size,
    jint srcRate,
    jint dstRate
) {
    LOGD("初始化李凭");
    LipinConfig* config    = new LipinConfig();
    config->bits           = bits;
    config->srcBytesLength = size;
    config->src_rate       = srcRate;
    config->dst_rate       = dstRate;
    config->init();
    return (jlong) config;
}

JNIEXPORT jbyteArray JNICALL
Java_com_acgist_lipin_Lipin_Rnnoise(
    JNIEnv* env,
    jobject lipin,
    jlong      pointer,
    jbyteArray pcm
) {
    LipinConfig* config = (LipinConfig*) pointer;
    jbyte* srcBytes = env->GetByteArrayElements(pcm, 0);
    int bytesLength = env->GetArrayLength(pcm);
    uint8_t* src = new uint8_t[bytesLength];
    for (int i = 0; i < bytesLength; i++) {
        src[i] = (uint8_t) srcBytes[i];
    }
    swr_convert(config->swr_context, &config->dstBuffer, config->dst_nb_samples, (const uint8_t**) &src, config->src_nb_samples);
//    for (int i = 0; i < config->dstBytesLength; i++) {
//        config->rnnoiseSrc[i] = config->dstBuffer[i];
//    }
    for (int i = 0; i < config->dstBytesLength; i+=2) {
        config->rnnoiseSrc[i] = config->dstBuffer[i];
    }
//    memcpy(config->rnnoiseDst, config->rnnoiseSrc, config->dstBytesLength);
    rnnoise_process_frame(config->denoise_state, config->rnnoiseDst, config->rnnoiseSrc);
//    for (int i = 0; i < config->dstBytesLength; i++) {
//        config->dstBuffer[i] = config->rnnoiseDst[i];
//    }
    for (int i = 0; i < config->dstBytesLength; i+=2) {
        config->dstBuffer[i] = config->rnnoiseDst[i];
    }
    swr_convert(config->swr_context_restore, &config->srcBuffer, config->src_nb_samples, (const uint8_t**) &config->dstBuffer, config->dst_nb_samples);
    jbyteArray result = env->NewByteArray(config->srcBytesLength);
    jbyte dstBytes[config->srcBytesLength];
    for (int i = 0; i < config->srcBytesLength; i++) {
        dstBytes[i] = (int8_t) config->srcBuffer[i];
    }
    env->SetByteArrayRegion(result, 0, config->srcBytesLength, dstBytes);
    delete[] src;
//  env->DeleteLocalRef(result);
//  env->ReleaseByteArrayElements(result, dstBytes, 0);
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_acgist_lipin_Lipin_Resample(
    JNIEnv* env,
    jobject lipin,
    jlong      pointer,
    jbyteArray pcm
) {
    LipinConfig* config = (LipinConfig*) pointer;
    jbyte* srcBytes = env->GetByteArrayElements(pcm, 0);
    int bytesLength = env->GetArrayLength(pcm);
    uint8_t* src = new uint8_t[bytesLength];
    for (int i = 0; i < bytesLength; i++) {
        src[i] = (uint8_t) srcBytes[i];
    }
    swr_convert(config->swr_context, &config->dstBuffer, config->dst_nb_samples, (const uint8_t**) &src, config->src_nb_samples);
    jbyteArray result = env->NewByteArray(config->dstBytesLength);
    jbyte dstBytes[config->dstBytesLength];
    for (int i = 0; i < config->dstBytesLength; i++) {
        dstBytes[i] = (int8_t) config->dstBuffer[i];
    }
    env->SetByteArrayRegion(result, 0, config->dstBytesLength, dstBytes);
    delete[] src;
//  env->DeleteLocalRef(result);
//  env->ReleaseByteArrayElements(result, dstBytes, 0);
    return result;
}

JNIEXPORT void JNICALL
Java_com_acgist_lipin_Lipin_Release(
    JNIEnv* env,
    jobject lipin,
    jlong   pointer
) {
    LOGD("释放资源李凭");
    LipinConfig* config = (LipinConfig*) pointer;
    delete config;
}

}

