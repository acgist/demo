package com.acgist.hls.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * M3U8文件=TS索引
 * 
 * https://www.sohu.com/a/151089405_781333
 * https://blog.csdn.net/occupy8/article/details/43115765
 */
@Slf4j
@Getter
@Setter
public class M3u8 {

    /**
     * M3U8开始
     */
    private static final String M3U8_A =
        "#EXTM3U\r\n"                 +
        "#EXT-X-VERSION:3\r\n"        +
        "#EXT-X-ALLOW-CACHE:YES\r\n"  +
        "#EXT-X-TARGETDURATION:20\r\n" +
        "#EXT-X-MEDIA-SEQUENCE:0\r\n" +
        "#EXT-X-DISCONTINUITY\r\n";
    /**
     * M3U8结束
     */
    private static final String M3U8_Z = "#EXT-X-ENDLIST";
    /**
     * 最大分片时间
     */
    private static final long MAX_DURATION = 10L * 1000_000;
    /**
     * 定时任务
     */
    private static final ScheduledExecutorService SCHEDULED = Executors.newScheduledThreadPool(2);
    
    /**
     * 索引文件
     */
    private final Map<String, Ts> index;
    /**
     * M3U8信息
     */
    private final StringBuilder content;
    
    /**
     * 最后读取文件偏移
     */
    private int pos;
    /**
     * TS分片偏移
     */
    private int tsPos;
    /**
     * 最后TS偏移
     */
    private int lastTsPos;
    /**
     * TS分片索引
     */
    private int tsIndex;
    /**
     * TS分片时间
     */
    private long tsDuration;
    /**
     * 文件
     */
    private String file;
    /**
     * 结束文件
     */
    private String closeFile;
    /**
     * 是否是流媒体
     */
    private boolean stream = true;
    /**
     * 最后访问时间
     */
    private long lastAccessTime;
    /**
     * 输入流
     */
    private RandomAccessFile input;
    /**
     * 定时任务
     */
    private ScheduledFuture<?> scheduled;
    
    public M3u8() {
        this.index   = new HashMap<>();
        this.content = new StringBuilder();
    }
    
    /**
     * TS文件
     */
    @Getter
    @Setter
    public static class Ts {
        
        /**
         * 文件开始偏移
         */
        private int pos;
        /**
         * 文件长度
         */
        private int length;
        /**
         * 文件时长
         */
        private double duration;
        
        public Ts(int pos, int length, double duration) {
            this.pos      = pos;
            this.length   = length;
            this.duration = duration;
        }
        
    }
    
    /**
     * 加载文件
     * 
     * @param file 文件名称
     * @param path 文件路径
     * 
     * @throws FileNotFoundException 文件无效异常
     */
    public void load(String file, String path) throws FileNotFoundException {
        final int index = path.lastIndexOf('.');
        this.file       = file;
        this.closeFile  = path.substring(0, index) + ".mp4";
        this.input      = new RandomAccessFile(path, "r");
        this.checkStream();
        this.buildM3u8();
        if(this.stream) {
            log.debug("流式文件：{}", path);
            this.scheduled = SCHEDULED.scheduleWithFixedDelay(this::buildM3u8, 1000, 1000, TimeUnit.MILLISECONDS);
        } else {
            log.debug("普通文件：{}", path);
        }
    }
    
    /**
     * @return 是否可以关闭
     */
    public boolean closeable() {
        return System.currentTimeMillis() - this.lastAccessTime > 10L * 60 * 1000;
    }
    
    /**
     * 关闭资源
     */
    public void close() {
        log.debug("关闭M3U8：{}", this.file);
        IOUtils.closeQuietly(this.input);
        this.closeScheduled();
    }
    
    /**
     * 关闭定时任务
     */
    private void closeScheduled() {
        if(this.scheduled != null) {
            try {
                this.scheduled.cancel(true);
            } catch (Exception e) {
                log.error("关闭定时任务异常", e);
            }
        }
    }
    
    /**
     * 解析M3U8文件
     */
    private void buildM3u8() {
        synchronized (this.content) {
            if(this.pos <= 0L) {
                this.content.append(M3U8_A);
            }
            // PAT = 0000 = 0x01 = 188
            // PMT = 0042 = 0x02 = 188
            // PCR = 0101 = 0    = 188
            // PES = 0045 = 1+   = 188
            try {
                final byte[]     bytes        = new byte[188];
                final ByteBuffer typeBuffer   = ByteBuffer.allocate(2);
                while(this.pos < this.input.length()) {
                    this.input.seek(this.pos);
                    this.input.read(bytes);
                    this.pos   += bytes.length;
                    this.tsPos += bytes.length;
                    typeBuffer.put(bytes[1]);
                    typeBuffer.put(bytes[2]);
                    typeBuffer.flip();
                    final int type = typeBuffer.getShort() & 0B0001111111111111;
                    typeBuffer.compact();
                    switch(type) {
                    case 0x0000:
                    case 0x0042:
                        break;
                    case 0x0101:
                    case 0x0045:
                        this.buildTs(bytes);
                        break;
                    }
                }
            } catch (IOException e) {
                log.error("读取M3U8文件", e);
            }
            this.checkStream();
            if(this.tsPos > 0) {
                if(this.stream) {
                    // 没有处理
                } else {
                    this.buildTs();
                }
                this.pos        = this.lastTsPos;
                this.tsPos      = 0;
                this.tsDuration = 0L;
            }
            if(this.stream) {
                // 没有处理
            } else {
                this.content.append(M3U8_Z);
                this.closeScheduled();
            }
        }
    }
    
    /**
     * 构建TS分片
     * 
     * @param bytes 当前数据
     */
    private void buildTs(byte[] bytes) {
        long pts;
        if((bytes[3] & 0x0F) != 0) {
            int index = 13;
            pts = 0L;
            pts = (pts << 0) | ((bytes[index]     & 0B0000_1110L) >>> 1);
            pts = (pts << 8) | ((bytes[index + 1] & 0B1111_1111L));
            pts = (pts << 7) | ((bytes[index + 2] & 0B1111_1110L) >>> 1);
            pts = (pts << 8) | ((bytes[index + 3] & 0B1111_1111L));
            pts = (pts << 7) | ((bytes[index + 4] & 0B1111_1110L) >>> 1);
            if(((bytes[7] >>> 6) & 0B00000011) >= 0) {
                pts             /= 90000;
                this.tsDuration += pts;
            }
        }
        if(this.tsDuration >= MAX_DURATION) {
            this.buildTs();
        }
    }
    
    /**
     * 构建TS分片
     */
    private void buildTs() {
        final String name     = String.format("%06d.ts", this.tsIndex++);
        final double duration = this.tsDuration * 1D / 1000_000;
        final Ts ts = new Ts(this.lastTsPos, this.tsPos, duration);
        this.content.append(String.format("#EXTINF:%f,\r\n%s\r\n", duration, name));
        log.debug("解析TS文件：{} - {} - {}", ts.getPos(), ts.getLength(), ts.getDuration());
        this.index.put(name, ts);
        this.tsPos      = 0;
        this.tsDuration = 0L;
        this.lastTsPos  = this.pos;
    }

    /**
     * 读取M3U8文件
     * 
     * @param output 输出流
     * 
     * @throws IOException IO异常
     */
    public void readM3u8(OutputStream output) throws IOException {
        if(this.stream) {
            synchronized (this.content) {
                output.write(this.content.toString().getBytes());
                output.flush();
            }
        } else {
            output.write(this.content.toString().getBytes());
        }
    }
    
    /**
     * 读取TS文件
     * 
     * @param file   文件
     * @param output 输出流
     * 
     * @throws IOException IO异常
     */
    public void readTs(String file, OutputStream output) throws IOException {
        final Ts ts = this.index.get(file);
        if(ts == null) {
            log.warn("TS文件失效：{}", file);
            return;
        }
        synchronized (this.input) {
            final byte[] bytes = new byte[ts.length];
            this.input.seek(ts.pos);
            this.input.read(bytes);
            output.write(bytes);
        }
        this.lastAccessTime = System.currentTimeMillis();
    }
    
    /**
     * @return 是否流式文件
     */
    private boolean checkStream() {
        if(this.stream) {
            this.stream = !Paths.get(this.closeFile).toFile().exists();
        }
        return this.stream;
    }
    
}
