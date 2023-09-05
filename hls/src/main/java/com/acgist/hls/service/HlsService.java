package com.acgist.hls.service;

import com.acgist.hls.service.impl.M3u8;

import jakarta.servlet.http.HttpServletResponse;

public interface HlsService {
    
    /**
     * 预热数据
     * 
     * @param file M3U8文件
     * 
     * @return M3U8
     */
    M3u8 preheat(String file);
    
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
