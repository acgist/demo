package com.acgist.hls.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.acgist.hls.service.HlsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HlsServiceImpl implements HlsService {

    /**
     * 文件基础路径
     */
    @Value("${ts.path:D:/download}")
    private String rootPath;
    /**
     * 缓存文件
     */
    private Map<String, M3u8> map = new ConcurrentHashMap<>();
    
    @Scheduled(initialDelay = 60000, fixedDelay = 300000)
    public void scheduled() {
        final List<String> keys = this.map.entrySet().stream()
            .filter(v -> v.getValue().closeable())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        keys.forEach(key -> {
            final M3u8 m3u8 = this.map.remove(key);
            m3u8.close();
        });
    }
    
    @Override
    public M3u8 preheat(String file) {
        return this.map.computeIfAbsent(file, key -> {
            final M3u8 m3u8 = new M3u8();
            try {
                m3u8.load(file, Paths.get(this.rootPath, file).toAbsolutePath().toString());
            } catch (FileNotFoundException e) {
                log.error("加载文件异常：{}", file, e);
            }
            return m3u8;
        });
    }
    
    @Override
    public void indexM3u8(String file, HttpServletResponse response) {
        try {
            this.preheat(file).readM3u8(response.getOutputStream());
        } catch (IOException e) {
            log.error("写出M3U8文件异常：{}", file, e);
        }
    }

    @Override
    public void indexTs(String file, String ts, HttpServletResponse response) {
        final M3u8 m3u8 = this.map.get(file);
        if(m3u8 == null) {
            log.warn("读取M3U8文件失败：{} - {}", file, ts);
            return;
        }
        try {
            m3u8.readTs(ts, response.getOutputStream());
        } catch (IOException e) {
            log.error("写出TS文件异常：{} - {}", file, ts, e);
        }
    }

}
