package com.acgist.report.word.module;

import java.util.stream.Stream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.acgist.data.report.entity.ReportModelInstance;
import com.acgist.report.word.WordModule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 文本
 */
@Slf4j
@Setter
public class TextWordModule extends WordModule {

    // 是否使用HTML格式
    private boolean html;
    // 文本内容
    private String text;
    
    public TextWordModule(boolean html, ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
        this.html = html;
    }

    @Override
    public void buildModule() {
//      this.createTitle();
        if(this.html) {
            this.createHtml(this.text);
        } else {
            this.createText(this.text);
        }
    }

    /**
     * 生成HTML
     * 
     * @param html HTML
     */
    private void createHtml(String html) {
        final Document document = Jsoup.parse(html);
        final Elements elements = document.body().children();
        for (int index = 0; index < elements.size(); index++) {
            final Element element = elements.get(index);
            final String tagName = element.tagName();
            switch (tagName) {
            case "h1":
            case "h2":
            case "h3":
            case "h4":
            case "h5":
            case "h6":
                this.createTitle(element.text());
                break;
            default:
                final XWPFRun run = this.createText(element.text());
                this.setStyle(run, element);
                if(index + 1 < elements.size()) {
                    run.addBreak();
                }
                break;
            }
        }
    }
    
    /**
     * 设置样式
     * 
     * @param run     Docx元素
     * @param element HTML元素
     */
    private void setStyle(XWPFRun run, Element element) {
        if(!element.hasAttr("style")) {
            return;
        }
        Stream.of(element.attr("style").split(";")).forEach(style -> {
            final String key = style.substring(0, style.indexOf(':')).strip();
            final String val = style.substring(style.indexOf(':') + 1).strip();
            switch (key) {
            case "color":
                String color = val.replace("#", "");
                if(color.length() == 6) {
                    run.setColor(color);
                } else if(color.length() == 3) {
                    run.setColor(
                        color.substring(0, 1).repeat(2) +
                        color.substring(1, 2).repeat(2) +
                        color.substring(2, 3).repeat(2)
                    );
                } else {
                    log.warn("颜色格式错误：{}", color);
                }
                break;
            case "font-size":
                run.setFontSize(Integer.parseInt(val.replace("px", "").strip()));
                break;
            case "font-weight":
                run.setBold("bold".equals(val));
                break;
            default:
                log.debug("没有适配样式：{} - {}", key, val);
                break;
            }
        });
    }
    
}
