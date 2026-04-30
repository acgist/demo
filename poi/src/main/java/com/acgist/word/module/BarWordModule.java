package com.xyh.pemc.southbound.report.word.module;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChart;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB;

import com.xyh.pemc.southbound.data.report.entity.ReportModelInstance;
import com.xyh.pemc.southbound.report.word.WordModule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 柱状图
 */
@Slf4j
@Setter
public class BarWordModule extends WordModule {

    // 数据
    private Map<String, Integer> data;
    
    public BarWordModule(ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
    }

    @Override
    public void buildModule() {
        this.createTitle();
        try {
            // 创建图表
            final XWPFRun run = this.content.createRun();
            final XWPFChart chart = this.document.createChart(run, XDDFChart.DEFAULT_WIDTH * 11, XDDFChart.DEFAULT_HEIGHT * 6);
            if(this.title != null) {
                chart.setTitleText(this.title.getText());
            }
            chart.setTitleOverlay(false);
            // 图例
            final XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.RIGHT);
            final XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            final XDDFValueAxis valuesAxis = chart.createValueAxis(AxisPosition.LEFT);
            valuesAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
            final XDDFBarChartData barChart = (XDDFBarChartData) chart.createData(ChartTypes.BAR, categoryAxis, valuesAxis);
            // 数据
            final XDDFDataSource<String> category = XDDFDataSourcesFactory.fromArray(this.data.keySet().toArray(String[]::new));
            final XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(this.data.values().toArray(Integer[]::new));
            barChart.addSeries(category, values);
            barChart.setVaryColors(true);
            barChart.setBarDirection(BarDirection.COL);
            // 画图
            chart.plot(barChart);
            // 样式
            final CTBarChart[] barCharts = chart.getCTChart().getPlotArea().getBarChartArray();
            for (CTBarChart ctBarChart : barCharts) {
                final CTBarSer[] ctBarSers = ctBarChart.getSerArray();
                for (CTBarSer ctPieSer : ctBarSers) {
                    final CTDLbls ctdLbls = ctPieSer.addNewDLbls();
                    ctdLbls.addNewShowVal().setVal(true);
                    ctdLbls.addNewShowCatName().setVal(false);
                    ctdLbls.addNewShowSerName().setVal(false);
                    ctdLbls.addNewShowPercent().setVal(false);
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
