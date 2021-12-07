package com.acgist.poi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import com.acgist.poi.ExcelChartUtils.Chart;
import com.acgist.poi.ExcelChartUtils.LineChart;
import com.acgist.poi.ExcelChartUtils.PieChart;

public class ExcelCDhartUtilsTest {

	@Test
	public void testChart() throws IOException {
		final List<Chart> charts = new ArrayList<>();
		final LineChart singleLineChart = new LineChart("张三", 5, 20);
		singleLineChart.setYAxis(List.of("成绩"));
		singleLineChart.setXAxis(List.of("第一学期"));
		singleLineChart.setData(List.of(List.of(100)));
		charts.add(singleLineChart);
		final LineChart scoreChart = new LineChart("张三", 5, 20);
		scoreChart.setYAxis(List.of("成绩"));
		scoreChart.setXAxis(List.of("第一学期", "第二学期", "第三学期", "第四学期"));
		scoreChart.setData(List.of(List.of(100, 90, 88, 100)));
		charts.add(scoreChart);
		final LineChart salesChart = new LineChart("价格", 5, 20);
		salesChart.setYAxis(List.of("苹果", "李子", "橘子"));
		salesChart.setXAxis(List.of("第一季度", "第二季度", "第三季度", "第四季度"));
		salesChart.setData(List.of(List.of(20, 21, 22, 23), List.of(30, 20, 25, 30), List.of(20, 30, 20, 18)));
		charts.add(salesChart);
		final PieChart singlePieChart = new PieChart("异常分类", 5, 20);
		singlePieChart.setTitle(Arrays.asList("线路"));
		singlePieChart.setData(Arrays.asList(15));
		charts.add(singlePieChart);
		final PieChart alarmChart = new PieChart("异常分类", 5, 20);
		alarmChart.setTitle(Arrays.asList("线路", "光路", "湿度", "温度"));
		alarmChart.setData(Arrays.asList(15, 1, 24, 6));
		charts.add(alarmChart);
//		final XSSFWorkbook workbook = ExcelChartUtils.load("E:/tmp/告警导出.xlsx");
//		ExcelChartUtils.buildChart("图表", workbook, charts);
		final XSSFWorkbook workbook = new XSSFWorkbook();
		ExcelChartUtils.buildChart("图表", workbook, charts);
		ExcelChartUtils.write("E:/tmp/chart.xlsx", workbook);
	}
	
}
