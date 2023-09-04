package com.acgist.hls;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import com.acgist.hls.service.impl.M3u8;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class M3u8Test {

    @Test
    public void testM3u8() throws FileNotFoundException, InterruptedException {
        final M3u8 m3u8 = new M3u8();
        m3u8.load("1.ts", "D:\\tmp\\1.ts");
        Thread.sleep(5000L);
        log.info("{}", m3u8.getContent());
    }
    
    @Test
    public void testPts() {
//      final byte[] bytes = { 0x21, 0x00, 0x01, 0x1E, 0x01 };
//      final byte[] bytes = { 0x21, 0x00, 0x01, 0x2D, (byte) 0x01 };
        final byte[] bytes = { 0x21, 0x00, 0x01, 0x17, 0x71 };
        long pts   = 0L;
        int  index = 0;
        pts = (pts << 0) | ((bytes[index]     & 0B0000_1110L) >>> 1);
        pts = (pts << 8) | ((bytes[index + 1] & 0B1111_1111L));
        pts = (pts << 7) | ((bytes[index + 2] & 0B1111_1110L) >>> 1);
        pts = (pts << 8) | ((bytes[index + 3] & 0B1111_1111L));
        pts = (pts << 7) | ((bytes[index + 4] & 0B1111_1110L) >>> 1);
        log.info("{}", pts * 1D / 90000);
    }
    
}
