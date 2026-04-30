package com.acgist.doc.spliter;

import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * 知识库切割机
 */
public interface DocSpliter {
    
    /**
     * @param file 文件路径
     * 
     * @return 是否支持文件
     */
    default boolean isSupport(String file) {
        final int index = file.lastIndexOf('.');
        final List<String> supportExts = this.getSupportExts();
        if(CollectionUtils.isEmpty(supportExts)) {
            return false;
        }
        return supportExts.contains(file.substring(index));
    }
    
    /**
     * @return 支持文件后缀
     */
    List<String> getSupportExts();
    
    /**
     * 解析文件
     * 
     * @param path 文件路径
     * 
     * @return 文件列表
     */
    List<String> split(String path);

}
