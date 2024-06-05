package com.acgist.report.word.module;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.acgist.data.report.entity.ReportModelInstance;
import com.acgist.report.word.WordModule;

/**
 * 异常
 */
public class ErrorWordModule extends WordModule {

    // 错误信息
    private final String message;
    
    public ErrorWordModule(String message, ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
        this.message = message;
    }

    @Override
    public void buildModule() {
        this.createTitle(this.instance.getModelName());
        this.createText(this.message);
    }

}
