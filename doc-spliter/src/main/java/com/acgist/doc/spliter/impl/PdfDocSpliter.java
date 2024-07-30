package com.acgist.doc.spliter.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.doc.spliter.DocSpliterAdapter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PdfDocSpliter extends DocSpliterAdapter {

    /**
     * 最后一页
     */
    private int lastPage = 1;
    
    @Override
    public List<String> getSupportExts() {
        return List.of(".pdf");
    }

    @Override
    public List<String> split(String path) {
        final File file = new File(path);
        try(
            final InputStream input = new FileInputStream(file);
        ) {
            this.filename = file.getName();
            final PdfReader pdfReader = new PdfReader(input);
            this.readBookmark(pdfReader);
            pdfReader.close();
        } catch (IOException e) {
            log.error("解析PDF文件异常：{}", path, e);
        }
        return this.splitFileList;
    }

    /**
     * @param pdfReader PDF阅读器
     * 
     * @throws IOException IO异常
     */
    private void readBookmark(PdfReader pdfReader) throws IOException {
        final List<HashMap<String, Object>> bookmark = SimpleBookmark.getBookmark(pdfReader);
        for (final HashMap<String, Object> map : bookmark) {
            this.readBookmark(pdfReader, map);
        }
    }
    
    /**
     * @param pdfReader PDF阅读器
     * @param map       标签
     * 
     * @throws IOException IO异常
     */
    private void readBookmark(PdfReader pdfReader, Map<?, ?> map) throws IOException {
        final Object kids   = map.get("Kids");
        final String page   = map.get("Page").toString();
        final String title  = map.get("Title").toString();
        final int pageValue = Integer.parseInt(page.substring(0, page.indexOf(' ')));
        this.flushPage(pageValue, title, pdfReader);
        if(kids instanceof List<?> list) {
            for(final Object v : list) {
                this.readBookmark(pdfReader, (Map<?, ?>) v);
            }
        }
    }
    
    /**
     * 提取分页
     * 
     * @param lastPage  最后一夜
     * @param title     标题
     * @param pdfReader PDF阅读器
     * 
     * @throws IOException IO异常
     */
    private void flushPage(int lastPage, String title, PdfReader pdfReader) throws IOException {
        log.info("读取标签：{} - {} - {}", title, lastPage, this.lastPage);
        final PdfReaderContentParser pdfParser = new PdfReaderContentParser(pdfReader);
        TextExtractionStrategy strategy;
        for (int page = this.lastPage; page <= lastPage; page++) {
            strategy = pdfParser.processContent(page, new SimpleTextExtractionStrategy());
            this.write(strategy.getResultantText());
        }
        this.lastPage = lastPage;
    }
    
}
