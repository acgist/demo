package com.acgist.lipin;

/**
 * 李凭配置
 *
 * @author acgist
 */
public class Lipin {

    /**
     * 指针
     */
    private long pointer;

    /**
     * @see #Init(int, int, int, int)
     */
    public void init(int bits, int size, int srcSamples, int dstSamples) {
        this.pointer = this.Init(bits, size, srcSamples, dstSamples);
    }

    /**
     * @see #Rnnoise(long, byte[])
     */
    public byte[] rnnoise(byte[] pcm) {
        return this.Rnnoise(this.pointer, pcm);
    }

    /**
     * @see #Resample(long, byte[])
     */
    public byte[] resample(byte[] pcm) {
        return this.Resample(this.pointer, pcm);
    }

    /**
     * @see #Release(long)
     */
    public void release() {
        this.Release(this.pointer);
    }

    /**
     * 初始化
     *
     * @param bits    位深
     * @param size    缓冲大小
     * @param srcRate 原始采样率
     * @param dstRate 目标采样率
     *
     * @return config pointer
     */
    public native long Init(int bits, int size, int srcRate, int dstRate);

    /**
     * 降噪
     *
     * @param pointer 指针
     * @param pcm     PCM数据
     *
     * @return PCM数据
     */
    public native byte[] Rnnoise(long pointer, byte[] pcm);

    /**
     * 重采样
     *
     * @param pointer 指针
     * @param pcm     PCM数据
     *
     * @return PCM数据
     */
    public native byte[] Resample(long pointer, byte[] pcm);

    /**
     * 释放资源
     *
     * @param pointer 指针
     */
    public native void Release(long pointer);

}
