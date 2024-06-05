package com.acgist.report.word.module;

import java.io.IOException;
import java.util.List;
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
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;

import com.acgist.data.report.entity.ReportModelInstance;
import com.acgist.report.word.WordModule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 折线图
 */
@Slf4j
@Setter
public class LineWordModule extends WordModule {

    // 数据
    private Map<String, List<Integer>> data;
    
    public LineWordModule(ReportModelInstance instance, XWPFDocument document) {
        super(instance, document);
    }

    @Override
    public void buildModule() {
        this.createTitle();
        try {
            // 创建图表
            final XWPFRun run = this.paragraph.createRun();
            final XWPFChart chart = this.document.createChart(run, XDDFChart.DEFAULT_WIDTH * 10, XDDFChart.DEFAULT_HEIGHT * 5);
            chart.setTitleText(this.instance.getModelName());
            chart.setTitleOverlay(false);
            // 图例
            final XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);
            final XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            final XDDFValueAxis valuesAxis = chart.createValueAxis(AxisPosition.LEFT);
            valuesAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
            final XDDFLineChartData lineChart = (XDDFLineChartData) chart.createData(ChartTypes.LINE, categoryAxis, valuesAxis);
            // 数据
            this.data.forEach((k, v) -> {
                final XDDFDataSource<String> category = XDDFDataSourcesFactory.fromArray(this.data.keySet().toArray(String[]::new));
//              final XDDFDataSource<String> category = XDDFDataSourcesFactory.fromArray(new String[] { k });
                final XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(v.toArray(Integer[]::new));
                final XDDFLineChartData.Series series = (XDDFLineChartData.Series) lineChart.addSeries(category, values);
                series.setSmooth(false);
//              series.setMarkerStyle(MarkerStyle.NONE);
                series.setShowLeaderLines(false);
                lineChart.setVaryColors(true);
                // 样式
                final CTLineChart[] ctLineCharts = chart.getCTChart().getPlotArea().getLineChartArray();
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
                    }
                }
            });
            chart.plot(lineChart);
        } catch (InvalidFormatException | IOException e) {
            log.error("创建柱状图异常", e);
        }
    }

}
