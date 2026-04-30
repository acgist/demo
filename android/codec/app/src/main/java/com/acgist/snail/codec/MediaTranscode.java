package com.acgist.snail.codec;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 转码
 */
public class MediaTranscode {

    private static final String TAG = MediaTranscode.class.getSimpleName();

    private String path;
    private String outputPath;
    private MediaMuxer muxer;
    private MediaExtractor extractor;
    private boolean repairKeyFrame = true;
    private volatile boolean running = false;
    // 音频
    private int inputAudioIndex  = -1;
    private int outputAudioIndex = -1;
    private MediaCodec audioDecoder;
    private MediaCodec audioEncoder;
    private Thread audioDecodeThread;
    private Thread audioEncodeThread;
    private MediaFormat inputAudioFormat;
    private MediaFormat outputAudioFormat;
    private volatile boolean audioEnding  = true;
    private volatile boolean audioRunning = false;
    // 视频：拷贝不用编码解码
    private int inputVideoIndex  = -1;
    private int outputVideoIndex = -1;
    private MediaCodec videoDecoder;
    private MediaCodec videoEncoder;
    private Thread videoDecodeThread;
    private Thread videoEncodeThread;
    private MediaFormat inputVideoFormat;
    private MediaFormat outputVideoFormat;
    private volatile boolean videoEnding  = true;
    private volatile boolean videoRunning = false;

    public boolean transcode(String path) {
        return this.transcode(path, path + ".mp4");
    }

    public boolean transcode(String path, String outputPath) {
        this.path = path;
        this.outputPath = outputPath;
        final File file = new File(this.path);
        if(!file.exists()) {
            Log.w(TAG, "转码文件无效：" + this.path);
            return false;
        }
        try {
            this.init();
            this.initCodec();
            this.audioRunning = this.inputAudioIndex >= 0;
            this.videoRunning = this.inputVideoIndex >= 0;
            if(this.audioRunning) {
                this.audioEnding = false;
                this.extractor.selectTrack(this.inputAudioIndex);
//                this.extractor.seekTo(0, MediaExtractor.SEEK_TO_NEXT_SYNC);
                this.initAudioThread();
                this.transcodeAudioTrack();
                this.extractor.unselectTrack(this.inputAudioIndex);
            }
            if(this.videoRunning) {
                this.videoEnding = false;
                this.extractor.selectTrack(this.inputVideoIndex);
//                this.extractor.seekTo(0, MediaExtractor.SEEK_TO_NEXT_SYNC);
                this.initVideoThread();
                this.copyVideoTrack();
                this.extractor.unselectTrack(this.inputVideoIndex);
            }
            this.stop();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "转码异常：" + this.path, e);
        }
        return false;
    }

    private void init() throws IOException {
        this.muxer     = new MediaMuxer(this.outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        this.extractor = new MediaExtractor();
        this.extractor.setDataSource(this.path);
        final int trackCount = this.extractor.getTrackCount();
        int index = 0;
        do {
            final MediaFormat trackFormat = this.extractor.getTrackFormat(index);
            final String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if(mime.toLowerCase().contains("audio")) {
                this.inputAudioIndex   = index;
                this.inputAudioFormat  = trackFormat;
                this.outputAudioFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, 48000, 1);
                this.outputAudioFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000);
                this.outputAudioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
                this.outputAudioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 128 * 1024);
            } else if(mime.toLowerCase().contains("video")) {
                this.inputVideoIndex   = index;
                this.inputVideoFormat  = trackFormat;
                this.outputVideoFormat = trackFormat;
            } else {
                Log.w(TAG, "不支持的编码：" + mime);
            }
        } while(++index < trackCount);
    }

    private void initCodec() throws IOException {
        if(this.inputAudioIndex >= 0) {
            // 音频解码
            this.audioDecoder = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_AUDIO_OPUS);
            this.audioDecoder.configure(this.inputAudioFormat, null, null, 0);
            this.audioDecoder.start();
            // 音频编码
            this.audioEncoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            this.audioEncoder.configure(this.outputAudioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            this.audioEncoder.start();
        }
    }

    private void initAudioThread() {
        // 解码线程
        this.audioDecodeThread = new Thread(() -> {
            try {
                while (this.audioRunning) {
                    int index = this.audioDecoder.dequeueInputBuffer(10_000);
                    while (index < 0) {
                        if(this.audioRunning) {
                            index = this.audioDecoder.dequeueInputBuffer(10_000);
                        } else {
                            break;
                        }
                    }
                    if(!this.audioRunning) {
                        break;
                    }
                    final ByteBuffer buffer = this.audioDecoder.getInputBuffer(index);
                    final int size = this.extractor.readSampleData(buffer, 0);
                    if (size < 0) {
                        Log.d(TAG, "音频读取结束");
                        this.audioEnding = true;
                        // 不要手动添加结束符号
                        this.audioDecoder.queueInputBuffer(index, 0, 0, -1, -1);
                        break;
                    }
                    this.extractor.advance();
                    this.audioDecoder.queueInputBuffer(index, 0, size, this.extractor.getSampleTime(), this.extractor.getSampleFlags());
                }
            } catch (Exception e) {
                Log.e(TAG, "解码异常", e);
            }
        });
        // 编码线程
        this.audioEncodeThread = new Thread(() -> {
            try {
                final MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                while (this.audioRunning) {
                    int decoderIndex = this.audioDecoder.dequeueOutputBuffer(info, 10_000);
                    if (decoderIndex < 0) {
                        if (decoderIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                            continue;
                        } else {
                            continue;
                        }
                    }
                    final ByteBuffer decoderBuffer = this.audioDecoder.getOutputBuffer(decoderIndex);
                    // 开始编码
                    int encoderIndex = this.audioEncoder.dequeueInputBuffer(10_000);
                    while (encoderIndex < 0) {
                        if(this.audioRunning) {
                            encoderIndex = this.audioEncoder.dequeueInputBuffer(10_000);
                        } else {
                            break;
                        }
                    }
                    if(!this.audioRunning) {
                        break;
                    }
                    final ByteBuffer encoderBuffer = this.audioEncoder.getInputBuffer(encoderIndex);
                    encoderBuffer.put(decoderBuffer);
                    this.audioEncoder.queueInputBuffer(encoderIndex, 0, decoderBuffer.limit(), info.presentationTimeUs, info.flags);
                    this.audioDecoder.releaseOutputBuffer(decoderIndex, false);
                }
            } catch (Exception e) {
                Log.e(TAG, "编码异常", e);
            }
        });
        // 开始线程
        this.audioDecodeThread.start();
        this.audioEncodeThread.start();
    }

    private void copyAudioTrack() {
        // TODO
    }

    private void transcodeAudioTrack() {
        Log.d(TAG, "音频开始");
        final MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        while(this.audioRunning) {
            final int outputIndex = this.audioEncoder.dequeueOutputBuffer(info, 10_000);
            if(outputIndex < 0) {
                if(outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    Log.d(TAG, "开始合成器：" + this.path);
                    this.outputAudioIndex = this.muxer.addTrack(this.audioEncoder.getOutputFormat());
                    this.outputVideoIndex = this.muxer.addTrack(this.outputVideoFormat);
                    this.muxer.start();
                    this.running = true;
                    continue;
                } else {
                    this.audioRunning = !this.audioEnding;
                    if(this.audioEnding) {
                        Log.d(TAG, "音频编码结束");
                    }
                    continue;
                }
            } else {
                if(!this.running) {
                    this.audioEncoder.releaseOutputBuffer(outputIndex, false);
                    continue;
                }
            }
            // 去掉配置帧
            if((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                this.audioEncoder.releaseOutputBuffer(outputIndex, false);
                continue;
            }
            final ByteBuffer outputBuffer = this.audioEncoder.getOutputBuffer(outputIndex);
            this.muxer.writeSampleData(this.outputAudioIndex, outputBuffer, info);
            this.audioEncoder.releaseOutputBuffer(outputIndex, false);
//            Log.d(TAG, "音频：" + info.size + " = " + info.flags + " = " + info.presentationTimeUs);
            if((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                Log.d(TAG, "音频完成：" + info.size + " = " + info.flags + " = " + info.presentationTimeUs);
                this.audioRunning = false;
            }
        }
    }

    private void initVideoThread() {
    }

    private void copyVideoTrack() {
        Log.d(TAG, "视频开始");
        final ByteBuffer buffer = ByteBuffer.allocateDirect(128 * 1024);
        final MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        while(this.videoRunning) {
            final int size = this.extractor.readSampleData(buffer, 0);
            if (size < 0) {
                this.videoEnding  = true;
                this.videoRunning = false;
                break;
            }
            this.extractor.advance();
            info.size = size;
            // 修复关键帧
            if(this.repairKeyFrame) {
                info.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME;
                this.repairKeyFrame = false;
            } else {
                info.flags = this.extractor.getSampleFlags();
            }
            info.presentationTimeUs = this.extractor.getSampleTime();
            this.muxer.writeSampleData(this.outputVideoIndex, buffer, info);
//            Log.d(TAG, "视频：" + info.size + " = " + info.flags + " = " + info.presentationTimeUs);
            if((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                Log.d(TAG, "视频完成：" + info.size + " = " + info.flags + " = " + info.presentationTimeUs);
                this.videoRunning = false;
            }
        }
    }

    private void transcodeVideoTrack() {
        // TODO
    }

    public void stop() {
        Log.d(TAG, "转码结束：" + this.outputPath);
        this.running = false;
        this.audioRunning = false;
        this.videoRunning = false;
        try {
            if(this.audioDecoder != null) {
                this.audioDecoder.stop();
                this.audioDecoder.release();
            }
        } catch (Exception e) {
            Log.e(TAG, "结束音频解码器异常：" + this.path, e);
        }
        try {
            if(this.audioEncoder != null) {
                this.audioEncoder.stop();
                this.audioEncoder.release();
            }
        } catch (Exception e) {
            Log.e(TAG, "结束音编码器异常：" + this.path, e);
        }
        try {
            this.muxer.stop();
            this.muxer.release();
        } catch (Exception e) {
            Log.e(TAG, "结束混合器异常：" + this.path, e);
        }
        try {
            this.extractor.release();
        } catch (Exception e) {
            Log.e(TAG, "结束提取器异常：" + this.path, e);
        }
    }

}
