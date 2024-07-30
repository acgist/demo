package com.acgist.doc.spliter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DocSpliterAdapter implements DocSpliter {

    @Autowired
    protected DocSpliterProperties docSpliterProperties;
    
    /**
     * 文件序号
     */
    protected int index;
    /**
     * 文件名称
     */
    protected String filename;
    /**
     * 切片列表
     */
    protected final List<String> splitFileList;
    
    public DocSpliterAdapter() {
        this.index = 0;
        this.splitFileList = new ArrayList<>();
    }
    
    /**
     * @param content 切片内容
     * 
     * @throws IOException IO异常
     */
    protected void write(String content) throws IOException {
        if(StringUtils.isEmpty(this.filename)) {
            this.filename = UUID.randomUUID().toString();
        }
        final Path path = Paths.get(this.docSpliterProperties.getPath(), this.filename + "-" + (++this.index) + ".md");
        final Path parent = path.getParent();
        final File file = parent.toFile();
        if(!file.exists()) {
            file.mkdirs();
        }
        log.info("文件分片：{}", path);
        this.splitFileList.add(path.toAbsolutePath().toString());
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

}
