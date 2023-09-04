package com.acgist.hls.service;

import jakarta.servlet.http.HttpServletResponse;

public interface HlsService {
    
    /**
     * 解析M3U8
     * 
     * @param file     M3U8文件
     * @param response 响应
     */
    void indexM3u8(String file, HttpServletResponse response);
    
    /**
     * 解析TS
     * 
     * @param file     M3U8文件
     * @param ts       TS文件
     * @param response 响应
     */
    void indexTs(String file, String ts, HttpServletResponse response);
    
}
