package com.xyh.pemc.southbound.report.word.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.IRunBody;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xyh.pemc.southbound.data.report.entity.ReportModelInstance;
import com.xyh.pemc.southbound.report.word.WordModule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 文本
 */
@Slf4j
@Setter
public class TextWordModule extends WordModule {

    public enum Type {
        
        PL,
        TEXT,
        HTML,
        
    }
    
    // 格式
    private Type type;
    // 文本内容
    private String text;
    // HTML标签
    private Map<String, String> html;
    
    public TextWordModule(Type type, ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
        this.type = type;
    }

    @Override
    public void buildModule() {
        if(this.type == Type.HTML) {
            this.createHtml(this.text);
        } else if(this.type == Type.PL) {
            final String uuid = UUID.randomUUID().toString().replace("-", "");
            this.createText(String.format("{{%s}}", uuid));
            this.html.put(uuid, this.text);
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
        this.toWord(document.body());
    }
    
    /**
     * 转为Word
     * 
     * @param parent 父级元素
     */
    private void toWord(Element parent) {
        final Elements elements = parent.children();
        for (int index = 0; index < elements.size(); index++) {
            final Element element = elements.get(index);
            final String  tagName = element.tagName();
            switch (tagName) {
            case "h1":
            case "h2":
            case "h3":
            case "h4":
            case "h5":
            case "h6": {
                this.toWord(element.text(), element, this::createTitle, true);
                break;
            }
            case "p": {
                this.toWord(element.text(), element, this::createText, index + 1 < elements.size());
                break;
            }
            default:
                this.toWord(element);
                break;
            }
        }
    }
    
    /**
     * 转为Word
     * 
     * @param text     文本
     * @param element  元素
     * @param function 生成run
     * @param newLine  新行
     */
    private void toWord(String text, Element element, BiFunction<Boolean, String, XWPFRun> function, boolean newLine) {
        int left  = -1;
        int right = 0;
        final List<String> lines = new ArrayList<>();
        while((left = text.indexOf('【', right)) >= 0) {
            lines.add(text.substring(right, left));
            if((right = text.indexOf('】', left)) >= 0) {
                lines.add(text.substring(left, right + 1));
                right++;
            } else {
                lines.add(text.substring(left));
                break;
            }
        }
        if(left < 0) {
            lines.add(text.substring(right));
        }
        XWPFRun run = null;
        for(String v : lines) {
            if(StringUtils.isEmpty(v)) {
                continue;
            }
            run = function.apply(false, v);
            this.setStyle(run, element);
            if(v.indexOf('【') == 0 && v.indexOf('】') == v.length() - 1) {
                run.setColor("2E9BFF");
            }
        }
        if(newLine && run != null) {
            run.addBreak();
        }
        if(run == null) {
            function.apply(true, text);
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
            final int index = style.indexOf(':');
            if(index < 0) {
                return;
            }
            final String key = style.substring(0, index).strip();
            final String val = style.substring(index + 1).strip();
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
            case "text-align":
                final IRunBody parent = run.getParent();
                if(parent instanceof XWPFParagraph) {
                    final XWPFParagraph paragraph = (XWPFParagraph) parent;
                    paragraph.setAlignment("center".equals(val) ? ParagraphAlignment.CENTER : "left".equals(val) ? ParagraphAlignment.LEFT : ParagraphAlignment.RIGHT);
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
