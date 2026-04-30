package com.acgist.share.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        final String path = "D:/tmp/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try(
            final InputStream input   = file.getInputStream();
            final OutputStream output = new FileOutputStream(new File(path));
        ) {
            input.transferTo(output);
        }
        return path;
    }
    
}
