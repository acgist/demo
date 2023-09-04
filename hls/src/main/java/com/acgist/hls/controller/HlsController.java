package com.acgist.hls.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.hls.service.HlsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hls")
@RequiredArgsConstructor
public class HlsController {

    private final HlsService hlsService;
    
    // 20230101.mp4
    // 20230101.m3u8
    @GetMapping("/{file}/index.m3u8")
    public void indexM3u8(@PathVariable String file, HttpServletResponse response) {
        this.hlsService.indexM3u8(file, response);
    }
    
    // 20230101.mp4/000000.ts
    // 20230101.m3u8/000000.ts
    @GetMapping("/{file}/{ts}")
    public void indexTs(@PathVariable String file, @PathVariable String ts, HttpServletResponse response) {
        this.hlsService.indexTs(file, ts, response);
    }
    
}
