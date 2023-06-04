#include "resampling.hpp"

// https://www.jianshu.com/p/c2626d394b83
// https://blog.csdn.net/qq_38795209/article/details/107492009
// https://blog.csdn.net/m0_60259116/article/details/124865591
// https://www.ffmpeg.org/doxygen/2.0/doc_2examples_2resampling_audio_8c-example.html

extern "C" {

DenoiseState* state;

uint8_t* resampling(uint8_t **src_data = NULL, uint8_t **dst_data = NULL) {
    // 原始数据配置
    int64_t src_ch_layout = AV_CH_LAYOUT_STEREO;
    enum AVSampleFormat src_sample_fmt = AV_SAMPLE_FMT_S16;
    int src_rate = 8000;
    int src_linesize = 0;
    int src_nb_samples = 160;
    int src_nb_channels = av_get_channel_layout_nb_channels(src_ch_layout);
    // 目标数据配置
    int64_t dst_ch_layout = AV_CH_LAYOUT_STEREO;
    enum AVSampleFormat dst_sample_fmt = AV_SAMPLE_FMT_S16;
    int dst_rate = 48000;
    int dst_linesize = 0;
    int dst_nb_samples = av_rescale_rnd(src_nb_samples, dst_rate, src_rate, AV_ROUND_UP);;
    int dst_nb_channels = av_get_channel_layout_nb_channels(dst_ch_layout);
    // 配置重采样
    struct SwrContext *swr_ctx = swr_alloc();
    swr_alloc_set_opts(
        swr_ctx,
        // 输出通道
        dst_ch_layout,
        dst_sample_fmt,
        dst_rate,
        // 输入通道
        src_ch_layout,
        src_sample_fmt,
        src_rate,
        0,
        0
    );
    // 加载上下文
    swr_init(swr_ctx);
    // 输入缓冲区
    av_samples_alloc_array_and_samples(&src_data, &src_linesize, src_nb_channels, src_nb_samples, src_sample_fmt, 0);
    // 输出缓冲区
    av_samples_alloc_array_and_samples(&dst_data, &dst_linesize, dst_nb_channels, dst_nb_samples, dst_sample_fmt, 0);
    // 执行重采样
    int ret = swr_convert(swr_ctx, dst_data, dst_nb_samples, (const uint8_t **) src_data, src_nb_samples);
    return dst_data[0];
//    swr_free(&swr_ctx);
//    if (src_data) {
//        av_freep(&src_data[0]);
//    }
//    av_freep(&src_data);
//    if (dst_data) {
//        av_freep(&dst_data[0]);
//    }
//    av_freep(&dst_data);
}

JNIEXPORT void JNICALL
Java_com_acgist_resampling_Resampling_init(
    JNIEnv* env,
    jclass clazz
) {
    LOGD("初始化resampling");
    state = rnnoise_create(NULL);
}

JNIEXPORT jbyteArray JNICALL
Java_com_acgist_resampling_Resampling_rnnoise(
    JNIEnv* env,
    jclass clazz,
    jbyteArray pcm,
    jint simples
) {
    return nullptr;
}

JNIEXPORT jbyteArray JNICALL
Java_com_acgist_resampling_Resampling_resampling(
    JNIEnv* env,
    jclass clazz,
    jbyteArray pcm,
    jint srcSimples,
    jint dstSimples
) {
    jbyte *srcBytes = env->GetByteArrayElements(pcm, 0);
    int bytesLength = env->GetArrayLength(pcm);
    uint8_t *src = new uint8_t[bytesLength];
    for (int i = 0; i < bytesLength; i++) {
        src[i] = (uint8_t) srcBytes[i];
    }
    uint8_t *dst = nullptr;
    dst = resampling(&src, &dst);
//    rnnoise_process_frame(state, src, src);
//    acgist::resampling(&src, &src);
    int length = 864;
//    int length = 960;
    jbyteArray result = env->NewByteArray(length);
    jbyte dstBytes[length];
    for (int i = 0; i < length; i++) {
        dstBytes[i] = (int8_t) dst[i];
    }
    env->SetByteArrayRegion(result, 0, length, dstBytes);
//  env ->ReleaseFloatArrayElements(result, srcBytes, 0);
    delete[] src;
    delete[] dst;
    return result;
}

JNIEXPORT void JNICALL
Java_com_acgist_resampling_Resampling_release(
    JNIEnv* env,
    jclass clazz
) {
    LOGD("释放资源resampling");
    if(state != nullptr) {
        delete state;
    }
}

}

