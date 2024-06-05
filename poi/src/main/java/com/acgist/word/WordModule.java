package com.acgist.report.word;

import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.acgist.data.report.entity.ReportModelInstance;

/**
 * 抽象模块
 * 
 * https://blog.csdn.net/weixin_42151235/article/details/131169539
 * https://roytuts.com/how-to-generate-pie-chart-in-excel-using-apache-poi/
 */
public abstract class WordModule implements IWordModule {

    // 段落
    protected XWPFParagraph paragraph;
    // 文档
    protected final XWPFDocument document;
    // 实例
    protected final ReportModelInstance instance;
    
    public WordModule(ReportModelInstance instance, XWPFDocument document) {
        this.instance = instance;
        this.document = document;
        this.paragraph = document.createParagraph();
        this.setBaseStyle();
    }

    /**
     * 基本样式
     */
    protected void setBaseStyle() {
        this.paragraph.setSpacingAfter(200);
        this.paragraph.setSpacingBefore(200);
        this.paragraph.setIndentationLeft(100);
        this.paragraph.setIndentationRight(100);
        this.paragraph.setSpacingBetween(1.2, LineSpacingRule.AUTO);
    }
    
    protected void createNewLine() {
    }
    
    /**
     * 设置Title
     * 
     * @return XWPFRun
     */
    protected XWPFRun createTitle() {
        return this.createTitle(true, this.instance.getModelName());
    }

    /**
     * 设置Title
     * 
     * @param newLine 新的一行
     * 
     * @return XWPFRun
     */
    protected XWPFRun createTitle(boolean newLine) {
        return this.createTitle(newLine, this.instance.getModelName());
    }
    
    /**
     * 设置Title
     * 
     * @param title Title
     * 
     * @return XWPFRun
     */
    protected XWPFRun createTitle(String title) {
        return this.createTitle(true, title);
    }

    /**
     * 设置Title
     * 
     * @param newLine 新的一行
     * @param title   Title
     * 
     * @return XWPFRun
     */
    protected XWPFRun createTitle(boolean newLine, String title) {
        final XWPFRun run = this.paragraph.createRun();
        run.setBold(true);
        run.setFontSize(14);
        run.setText(title);
        if(newLine) {
            run.addBreak();
        }
        this.paragraph.addRun(run);
        return run;
    }
    
    /**
     * 设置文本
     * 
     * @param text 文本
     * 
     * @return XWPFRun
     */
    protected XWPFRun createText(String text) {
        final XWPFRun run = this.paragraph.createRun();
        run.setBold(false);
        run.setFontSize(10);
        run.setText(text);
        this.paragraph.addRun(run);
        return run;
    }
    
}
