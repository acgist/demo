package com.acgist.report.word.module;

import java.util.List;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableRowHeightRule;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;

import com.acgist.data.report.entity.ReportModelInstance;
import com.acgist.report.word.WordModule;

import lombok.Setter;

/**
 * 表格
 */
@Setter
public class TableWordModule extends WordModule {

    // 标题
    private List<String> header;
    // 数据
    private List<List<Object>> data;
    
    public TableWordModule(ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
    }

    @Override
    public void buildModule() {
        this.createTitle(false);
        final XWPFTable table = this.document.createTable(this.data.size() + 1, this.header.size());
        this.setStyle(table);
        this.createHeader(table);
        this.createData(table);
    }
    
    /**
     * 设置样式
     * 
     * @param table 表格
     */
    private void setStyle(XWPFTable table) {
        final String borderColor = "AAAAAA";
        table.setWidth(100);
        table.setWidthType(TableWidthType.PCT);
        final CTTblPr pr = table.getCTTbl().getTblPr();
        final CTTblBorders borders = pr.isSetTblBorders() ? pr.getTblBorders() : pr.addNewTblBorders();
        if(borders.isSetTop()) {
            borders.getTop().setColor(borderColor);
        } else {
            borders.addNewTop().setColor(borderColor);
        }
        if(borders.isSetLeft()) {
            borders.getLeft().setColor(borderColor);
        } else {
            borders.addNewLeft().setColor(borderColor);
        }
        if(borders.isSetRight()) {
            borders.getRight().setColor(borderColor);
        } else {
            borders.addNewRight().setColor(borderColor);
        }
        if(borders.isSetBottom()) {
            borders.getBottom().setColor(borderColor);
        } else {
            borders.addNewBottom().setColor(borderColor);
        }
        if(borders.isSetInsideH()) {
            borders.getInsideH().setColor(borderColor);
        } else {
            borders.addNewInsideH().setColor(borderColor);
        }
        if(borders.isSetInsideV()) {
            borders.getInsideV().setColor(borderColor);
        } else {
            borders.addNewInsideV().setColor(borderColor);
        }
    }
    
    /**
     * 设置样式
     * 
     * @param row 行
     */
    private void setHeaderStyle(XWPFTableRow row) {
        row.setHeight(400);
        row.setCantSplitRow(false);
        row.setHeightRule(TableRowHeightRule.EXACT);
    }
    
    /**
     * 设置样式
     * 
     * @param cell 单元格
     */
    private void setHeaderStyle(XWPFTableCell cell) {
        cell.setColor("F0F0F0");
        cell.setWidthType(TableWidthType.AUTO);
        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
        cell.getParagraphs().forEach(v -> {
            v.setAlignment(ParagraphAlignment.CENTER);
            v.setIndentationLeft(20);
            v.setIndentationRight(20);
            v.getRuns().forEach(x -> {
                x.setBold(true);
            });
        });
    }
    
    /**
     * 设置样式
     * 
     * @param row 行
     */
    private void setDataStyle(XWPFTableRow row) {
        row.setHeight(400);
        row.setCantSplitRow(false);
        row.setHeightRule(TableRowHeightRule.EXACT);
    }
    
    /**
     * 设置样式
     * 
     * @param cell 单元格
     */
    private void setDataStyle(XWPFTableCell cell) {
        cell.setWidthType(TableWidthType.AUTO);
        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
        cell.getParagraphs().forEach(v -> {
            v.setIndentationLeft(20);
            v.setIndentationRight(20);
        });
    }

    /**
     * 设置头部
     * 
     * @param table 表格
     */
    private void createHeader(XWPFTable table) {
        final XWPFTableRow row = table.getRow(0);
        for (int index = 0; index < this.header.size(); index++) {
            final XWPFTableCell cell = row.getCell(index);
            cell.setText(this.header.get(index));
            this.setHeaderStyle(cell);
        }
        this.setHeaderStyle(row);
    }
    
    /**
     * 设置数据
     * 
     * @param table 表格
     */
    private void createData(XWPFTable table) {
        for (int index = 0; index < this.data.size(); index++) {
            final List<Object> list = this.data.get(index);
            final XWPFTableRow row = table.getRow(1 + index);
            for (int jndex = 0; jndex < list.size(); jndex++) {
                final XWPFTableCell cell = row.getCell(jndex);
                cell.setText(Objects.toString(list.get(jndex), "-"));
                this.setDataStyle(cell);
            }
            this.setDataStyle(row);
        }
    }

}
