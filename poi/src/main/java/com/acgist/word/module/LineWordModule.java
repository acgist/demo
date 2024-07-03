package com.xyh.pemc.southbound.report.word.module;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChart;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB;

import com.xyh.pemc.southbound.data.report.entity.ReportModelInstance;
import com.xyh.pemc.southbound.report.word.WordModule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 折线图
 */
@Slf4j
@Setter
public class LineWordModule extends WordModule {

    // 维度
    private List<String> header;
    // 堆积
    private boolean heap = false;
    // 平滑
    private boolean smooth = false;
    // 数据
    private Map<String, List<Integer>> data;
    
    public LineWordModule(ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
    }

    @Override
    public void buildModule() {
        this.createTitle();
        if(this.heap) {
            this.doHeap();
        }
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
            final XDDFLineChartData lineChart = (XDDFLineChartData) chart.createData(ChartTypes.LINE, categoryAxis, valuesAxis);
            lineChart.setVaryColors(true);
            // 数据
            this.data.forEach((k, v) -> {
                final XDDFDataSource<String> category = XDDFDataSourcesFactory.fromArray(this.header.toArray(String[]::new));
                final XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(v.toArray(Integer[]::new));
                final XDDFLineChartData.Series series = (XDDFLineChartData.Series) lineChart.addSeries(category, values);
                series.setTitle(k, null);
                series.setSmooth(this.smooth);
                series.setMarkerStyle(MarkerStyle.NONE);
                series.setShowLeaderLines(false);
            });
            // 画图
            chart.plot(lineChart);
            // 样式
            final CTLineChart[] ctLineCharts = chart.getCTChart().getPlotArea().getLineChartArray();
            int index = 0;
            for (CTLineChart ctLineChart : ctLineCharts) {
                final CTLineSer[] ctLineSers = ctLineChart.getSerArray();
                for (CTLineSer ctLineSer : ctLineSers) {
                    final CTDLbls ctdLbls = ctLineSer.addNewDLbls();
                    ctdLbls.addNewShowVal().setVal(true);
                    ctdLbls.addNewShowCatName().setVal(false);
                    ctdLbls.addNewShowSerName().setVal(false);
                    ctdLbls.addNewShowPercent().setVal(false);
                    ctdLbls.addNewShowLegendKey().setVal(false);
                    ctdLbls.addNewShowLeaderLines().setVal(false);
                    // 颜色
                    ctLineSer.addNewIdx().setVal(index);
                    final STHexColorRGB color = STHexColorRGB.Factory.newInstance();
                    color.setStringValue(COLORS[index % COLORS.length]);
                    final CTLineProperties ctLineProperties = ctLineSer.addNewSpPr().addNewLn();
                    ctLineProperties.xsetW(STLineWidth.Factory.newValue(10000));
                    ctLineProperties.addNewSolidFill().addNewSrgbClr().xsetVal(color);
                    ++index;
                }
            }
        } catch (InvalidFormatException | IOException e) {
            log.error("创建柱状图异常", e);
        }
    }

    /**
     * 堆积图
     */
    private void doHeap() {
        final AtomicReference<List<Integer>> reference = new AtomicReference<>();
        this.data.forEach((k, v) -> {
            final List<Integer> old = reference.get();
            if(old != null) {
                for (int index = 0; index < v.size(); index++) {
                    v.set(index, old.get(index) + v.get(index));
                }
            }
            reference.set(v);
        });
    }
    
}
