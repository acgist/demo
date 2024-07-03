package com.xyh.pemc.southbound.report.word.module;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChart;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFPieChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB;

import com.xyh.pemc.southbound.data.report.entity.ReportModelInstance;
import com.xyh.pemc.southbound.report.word.WordModule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 饼图
 */
@Slf4j
@Setter
public class PieWordModule extends WordModule {

    // 数据
    private Map<String, Integer> data;
    
    public PieWordModule(ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
    }

    @Override
    public void buildModule() {
        this.createTitle();
        try {
            // 创建图表
            final XWPFRun run = this.content.createRun();
            final XWPFChart chart = this.document.createChart(run, XDDFChart.DEFAULT_WIDTH * 11, XDDFChart.DEFAULT_HEIGHT * 8);
            if(this.title != null) {
                chart.setTitleText(this.title.getText());
            }
            chart.setTitleOverlay(false);
            // 图例
            final XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);
            final XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            final XDDFValueAxis valuesAxis = chart.createValueAxis(AxisPosition.LEFT);
            valuesAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
            final XDDFPieChartData pieChart = (XDDFPieChartData) chart.createData(ChartTypes.PIE, categoryAxis, valuesAxis);
            // 数据
            final XDDFDataSource<String> category = XDDFDataSourcesFactory.fromArray(this.data.keySet().toArray(String[]::new));
            final XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(this.data.values().toArray(Integer[]::new));
            pieChart.addSeries(category, values);
            pieChart.setVaryColors(true);
            // 画图
            chart.plot(pieChart);
            // 样式
            final CTPieChart[] ctPieCharts = chart.getCTChart().getPlotArea().getPieChartArray();
            for (CTPieChart ctPieChart : ctPieCharts) {
                final CTPieSer[] ctPieSers = ctPieChart.getSerArray();
                for (CTPieSer ctPieSer : ctPieSers) {
                    final CTDLbls ctdLbls = ctPieSer.addNewDLbls();
                    ctdLbls.addNewShowVal().setVal(false);
                    ctdLbls.addNewShowCatName().setVal(false);
                    ctdLbls.addNewShowSerName().setVal(false);
                    ctdLbls.addNewShowPercent().setVal(true);
                    ctdLbls.addNewShowLegendKey().setVal(false);
                    ctdLbls.addNewShowLeaderLines().setVal(false);
                    // 颜色
                    for (int index = 0; index < this.data.size(); index++) {
                        final CTDPt ctdPt = ctPieSer.addNewDPt();
                        ctdPt.addNewIdx().setVal(index);
                        final STHexColorRGB color = STHexColorRGB.Factory.newInstance();
                        color.setStringValue(COLORS[index % COLORS.length]);
                        ctdPt.addNewSpPr().addNewSolidFill().addNewSrgbClr().xsetVal(color);
                    }
                }
            }
        } catch (InvalidFormatException | IOException e) {
            log.error("创建柱状图异常", e);
        }
    }

}
