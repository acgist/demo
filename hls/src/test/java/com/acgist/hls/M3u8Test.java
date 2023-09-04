package com.acgist.hls;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import com.acgist.hls.service.impl.M3u8;

public class M3u8Test {

    @Test
    public void testM3u8() throws FileNotFoundException, InterruptedException {
        final M3u8 m3u8 = new M3u8();
//      m3u8.load("index00000.ts", "D:\\tmp\\index00000.ts");
        m3u8.load("20230830181227.ts", "D:\\tmp\\20230830181227.ts");
//      m3u8.load("20230828073232.ts", "D:\\tmp\\20230828073232.ts");
//      m3u8.load("20230830181718.ts", "D:\\tmp\\20230830181718.ts");
        Thread.sleep(50000L);
        System.out.println(m3u8.getContent());
    }
    
}
