package com.acgist.report.word;

import java.io.File;

import com.acgist.data.report.entity.ReportHistory;

/**
 * Word工具
 */
public interface IWordService {

    /**
     * @param file          文件
     * @param reportHistory 历史报告
     * 
     * @return 是否成功
     */
    boolean build(File file, ReportHistory reportHistory);
    
}
