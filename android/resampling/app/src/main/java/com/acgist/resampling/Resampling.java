package com.acgist.resampling;

/**
 * 重采样和降噪
 *
 * @author acgist
 */
public class Resampling {

    /**
     * 初始化
     */
    public static native void init();

    /**
     * 降噪
     *
     * @param pcm     PCM数据
     * @param samples 采样率
     *
     * @return PCM数据
     */
    public static native byte[] rnnoise(byte[] pcm, int samples);

    /**
     * 重采样和降噪
     *
     * @param pcm        PCM数据
     * @param srcSamples 原始采样率
     * @param dstSamples 目标采样率
     *
     * @return PCM数据
     */
    public static native byte[] resampling(byte[] pcm, int srcSamples, int dstSamples);

    /**
     * 释放资源
     */
    public static native void release();

}
