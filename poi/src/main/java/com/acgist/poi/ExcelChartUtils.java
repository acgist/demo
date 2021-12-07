package com.acgist.poi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图表
 * 
 * @author yusheng
 */
public class ExcelChartUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelChartUtils.class);
	
	/**
	 * 加载XLSX
	 * 
	 * @param path 路径
	 * 
	 * @return XLSX
	 */
	public static final XSSFWorkbook load(final String path) {
		try(final InputStream input = new FileInputStream(path)) {
			return new XSSFWorkbook(input);
		} catch (IOException e) {
			LOGGER.error("加载XLSX异常：{}", path, e);
		}
		return null;
	}
	
	/**
	 * 保存XLSX
	 * 
	 * @param path 路径
	 * @param workbook XLSX
	 */
	public static final void write(final String path, final XSSFWorkbook workbook) {
		try (
			workbook;
			final OutputStream output = new FileOutputStream(path)
		) {
			workbook.write(output);
		} catch(IOException e) {
			LOGGER.error("保存XLSX异常：{}", path, e);
		}
	}
	
	/**
	 * 保存XLSX
	 * 
	 * @param output 输出流
	 * @param workbook XLSX
	 */
	public static final void write(final OutputStream output, final XSSFWorkbook workbook) {
		try(
			output;
			workbook;
		) {
			workbook.write(output);
		} catch(IOException e) {
			LOGGER.error("保存XLSX异常", e);
		}
	}
	
	/**
	 * 创建图表
	 * 
	 * @param name 图表名称
	 * @param workbook XLSX
	 * @param charts 图表数据
	 * 
	 * @return XLSX
	 */
	public static final XSSFWorkbook buildChart(XSSFWorkbook workbook, List<Chart> charts) {
		return buildChart(null, workbook, charts);
	}
	
	/**
	 * 创建图表
	 * 
	 * @param name 图表名称
	 * @param workbook XLSX
	 * @param charts 图表数据
	 * 
	 * @return XLSX
	 */
	public static final XSSFWorkbook buildChart(String name, XSSFWorkbook workbook, List<Chart> charts) {
		final XSSFSheet sheet;
		if (StringUtils.isEmpty(name)) {
			sheet = workbook.getSheetAt(0);
		} else {
			sheet = workbook.createSheet(name);
		}
		int lastRowNum = sheet.getLastRowNum() + 1;
		for (final Chart chart : charts) {
			final int chartRowNum = lastRowNum + chart.getPadding();
			final int chartRowNumEnd = chartRowNum + chart.getRow();
			final int chartColNum = chart.getPadding();
			final int chartColNumEnd = chartColNum + chart.getCol();
			final XSSFChart xssfChart = buildDrawingPatriarch(sheet, chartColNum, chartRowNum, chartColNumEnd, chartRowNumEnd, chart.getName());
			if(chart instanceof PieChart) {
				buildPieChart(xssfChart, (PieChart) chart);
			} else if(chart instanceof LineChart) {
				buildLineChart(xssfChart, (LineChart) chart);
			} else {
				LOGGER.warn("图表没有适配：{}", chart);
				continue;
			}
			lastRowNum = lastRowNum + chart.getPadding() + chart.getRow();
		}
		return workbook;
	}
	
	/**
	 * 创建图表
	 * 
	 * @param sheet sheet
	 * @param startCol 开始列
	 * @param startRow 开始行
	 * @param endCol 结束列
	 * @param endRow 结束行
	 * @param name 图表名称
	 * 
	 * @return 图表
	 */
	private static final XSSFChart buildDrawingPatriarch(XSSFSheet sheet, int startCol, int startRow, int endCol, int endRow, String name) {
		final XSSFDrawing drawing = sheet.createDrawingPatriarch();
		final XSSFClientAnchor anchor = drawing.createAnchor(
			0, 0, 0, 0,
			startCol, startRow, endCol, endRow
		);
		final XSSFChart chart = drawing.createChart(anchor);
		// 设置标题
		chart.setTitleText(name);
		// 设置直角
		if(chart.getCTChartSpace().isSetRoundedCorners()) {
			chart.getCTChartSpace().getRoundedCorners().setVal(false);
		} else {
			chart.getCTChartSpace().addNewRoundedCorners().setVal(false);
		}
		// 标题是否覆盖图表
		chart.setTitleOverlay(false);
		return chart;
	}

	/**
	 * 创建饼图
	 * 
	 * @param chart 图表
	 * @param pieChart 数据
	 */
	private static final void buildPieChart(XSSFChart chart, PieChart pieChart) {
		final List<String> title = pieChart.getTitle();
		final List<Integer> data = pieChart.getData();
		final XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.LEFT);
		// 设置维度
		final XDDFDataSource<String> dataSource = XDDFDataSourcesFactory.fromArray(title.toArray(String[]::new));
		final XDDFNumericalDataSource<Integer> numericalDataSource = XDDFDataSourcesFactory.fromArray(data.toArray(Integer[]::new));
		final XDDFChartData chartData = chart.createData(ChartTypes.PIE, null, null);
		chartData.setVaryColors(true);
		// 加载数据
		chartData.addSeries(dataSource, numericalDataSource);
		// 绘制图表
		chart.plot(chartData);
		// 设置样式
		chart.getCTChart().getPlotArea().getPieChartList().forEach(ctPieChart -> {
			ctPieChart.getSerList().forEach(ctPieSer -> {
				final CTDLbls ctdLbls;
				if(ctPieChart.isSetDLbls()) {
					ctdLbls = ctPieChart.getDLbls();
				} else {
					ctdLbls = ctPieChart.addNewDLbls();
				}
				setPieChartStyle(ctdLbls);
			});
		});
	}
	
	/**
	 * 设置样式
	 * 
	 * @param ctdLbls CTDLbls
	 */
	private static final void setPieChartStyle(CTDLbls ctdLbls) {
		// 设置换行：默认逗号
		ctdLbls.setSeparator("\n");
		if(ctdLbls.isSetShowVal()) {
			ctdLbls.getShowVal().setVal(false);
		} else {
			ctdLbls.addNewShowVal().setVal(false);
		}
		if(ctdLbls.isSetShowPercent()) {
			ctdLbls.getShowPercent().setVal(true);
		} else {
			ctdLbls.addNewShowPercent().setVal(true);
		}
		if(ctdLbls.isSetShowCatName()) {
			ctdLbls.getShowCatName().setVal(true);
		} else {
			ctdLbls.addNewShowCatName().setVal(true);
		}
		if(ctdLbls.isSetShowSerName()) {
			ctdLbls.getShowSerName().setVal(false);
		} else {
			ctdLbls.addNewShowSerName().setVal(false);
		}
		if(ctdLbls.isSetShowLegendKey()) {
			ctdLbls.getShowLegendKey().setVal(false);
		} else {
			ctdLbls.addNewShowLegendKey().setVal(false);
		}
		if(ctdLbls.isSetShowLeaderLines()) {
			ctdLbls.getShowLeaderLines().setVal(true);
		} else {
			ctdLbls.addNewShowLeaderLines().setVal(true);
		}
	}

	/**
	 * 创建折线图
	 * 
	 * @param chart 图表
	 * @param lineChart 数据
	 */
	private static final void buildLineChart(XSSFChart chart, LineChart lineChart) {
		final List<String> xAxisList = lineChart.getXAxis();
		final List<String> chartTitleList = lineChart.getYAxis();
		final List<List<Number>> chartDataList = lineChart.getData();
		final XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.TOP);
		// 设置维度
		final XDDFValueAxis valueAxis = chart.createValueAxis(AxisPosition.LEFT);
		final XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		final XDDFCategoryDataSource categoryDataSource = XDDFDataSourcesFactory.fromArray(xAxisList.toArray(String[]::new));
		final XDDFLineChartData chartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, categoryAxis, valueAxis);
		// 单行数据折线不变颜色
		if (chartDataList.size() == 1) {
			chartData.setVaryColors(false);
		}
		// 设置数据
		for (int index = 0; index < chartDataList.size(); index++) {
			final List<Number> list = chartDataList.get(index);
			final XDDFNumericalDataSource<Integer> numericalDataSource = XDDFDataSourcesFactory.fromArray(list.toArray(Integer[]::new));
			final XDDFLineChartData.Series series = (XDDFLineChartData.Series) chartData.addSeries(categoryDataSource, numericalDataSource);
			series.setTitle(chartTitleList.get(index), null);
			series.setSmooth(false);
			// 设置端点大小
//			series.setMarkerSize((short) 4); // 新版不能使用
			series.setMarkerStyle(MarkerStyle.CIRCLE);
		}
		// 绘制图表
		chart.plot(chartData);
	}
	
	/**
	 * 图表
	 * 
	 * @author yusheng
	 */
	public static class Chart {

		/**
		 * 标题
		 */
		private final String name;
		/**
		 * 间距
		 */
		private final int padding;
		/**
		 * 列数
		 */
		private final int col;
		/**
		 * 行数
		 */
		private final int row;

		public Chart(String name, int col, int row) {
			this(name, 1, col, row);
		}

		public Chart(String name, int padding, int col, int row) {
			this.name = name;
			this.padding = padding;
			this.col = col;
			this.row = row;
		}

		public String getName() {
			return name;
		}

		public int getPadding() {
			return padding;
		}

		public int getCol() {
			return col;
		}

		public int getRow() {
			return row;
		}
	}


	/**
	 * 饼图
	 * 
	 * @author yusheng
	 */
	public static final class PieChart extends Chart {

		/**
		 * 名称
		 */
		private List<String> title;
		/**
		 * 数据
		 */
		private List<Integer> data;
		
		public PieChart(String name, int col, int row) {
			super(name, col, row);
		}
		
		public PieChart(String name, int padding, int col, int row) {
			super(name, padding, col, row);
		}

		public List<String> getTitle() {
			return title;
		}

		public void setTitle(List<String> title) {
			this.title = title;
		}

		public List<Integer> getData() {
			return data;
		}

		public void setData(List<Integer> data) {
			this.data = data;
		}

	}

	/**
	 * 折线图
	 * 
	 * @author yusheng
	 */
	public static final class LineChart extends Chart {

		/**
		 * Y轴
		 */
		private List<String> yAxis;
		/**
		 * X轴
		 */
		private List<String> xAxis;
		/**
		 * 数据
		 */
		private List<List<Number>> data;
		
		public LineChart(String name, int col, int row) {
			super(name, col, row);
		}
		
		public LineChart(String name, int padding, int col, int row) {
			super(name, padding, col, row);
		}

		public List<String> getYAxis() {
			return yAxis;
		}

		public void setYAxis(List<String> yAxis) {
			this.yAxis = yAxis;
		}

		public List<String> getXAxis() {
			return xAxis;
		}

		public void setXAxis(List<String> xAxis) {
			this.xAxis = xAxis;
		}
		
		public List<List<Number>> getData() {
			return data;
		}
		
		public void setData(List<List<Number>> data) {
			this.data = data;
		}

	}

}
