package com.acgist.ding.view.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.acgist.ding.Config;
import com.acgist.ding.DingView;
import com.acgist.ding.Internationalization;
import com.acgist.ding.MapUtils;
import com.acgist.ding.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * JSON
 * 
 * @author acgist
 */
public class JsonView extends DingView {

	/**
	 * 原始JSON
	 */
	private StyledText source;
	/**
	 * 目标JSON
	 */
	private StyledText target;
	/**
	 * 排序
	 */
	private Button sort;
	/**
	 * 编码
	 */
	private Button encode;
	/**
	 * 解码
	 */
	private Button decode;
	
	/**
	 * GSON
	 */
	private final Gson gson = new Gson();
	/**
	 * Format GSON
	 */
	private final Gson formatGson = new GsonBuilder().setPrettyPrinting().create();
	
	@Override
	public void createPartControl(Composite composite) {
		this.layout(composite, SWT.HORIZONTAL);
		// 原始JSON模块
		final Composite sourceGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.source = this.buildMultiStyledText(sourceGroup, Internationalization.Message.JSON_SOURCE);
		// 操作模块
		final Group operationGroup = this.buildGroup(composite, Internationalization.Label.OPERATION, 2, 0, SWT.VERTICAL);
		// 配置模块
		final Composite configGroup = this.buildRowComposite(operationGroup, SWT.HORIZONTAL);
		this.sort = this.buildCheck(configGroup, Internationalization.Label.SORT);
		this.encode = this.buildCheck(configGroup, Internationalization.Label.ENCODE);
		this.decode = this.buildCheck(configGroup, Internationalization.Label.DECODE);
		// 按钮模块
		final Composite buttonGroup = this.buildRowComposite(operationGroup, SWT.HORIZONTAL);
		this.buildButton(buttonGroup, Internationalization.Button.JSON_FORMAT, this.formatConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.JSON_COMPRESS, this.compressConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.JSON_COMPARE, this.compareConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.JSON_TO_URL, this.jsonToUrlConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.URL_TO_JSON, this.urlToJsonConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.RESET, this.resetConsumer);
		// 目标JSON模块
		final Composite targetGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.target = this.buildMultiStyledText(targetGroup, Internationalization.Message.JSON_TARGET);
	}
	
	/**
	 * 格式化
	 */
	private Consumer<MouseEvent> formatConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final Map<String, Object> map = this.gson.fromJson(sourceValue, new TypeToken<Map<String, Object>>() {}.getType());
			final String targetValue = this.formatGson.toJson(map);
			this.target.setText(targetValue);
		});
	};
	
	/**
	 * 压缩
	 */
	private Consumer<MouseEvent> compressConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final Map<String, Object> map = this.gson.fromJson(sourceValue, new TypeToken<Map<String, Object>>() {}.getType());
			final String targetValue = this.gson.toJson(map);
			this.target.setText(targetValue);
		});
	};
	
	/**
	 * 比较
	 */
	private Consumer<MouseEvent> compareConsumer = event -> {
		this.consumer(this.source, this.target, (sourceValue, targetValue) -> {
			final Type type = new TypeToken<Map<String, Object>>() {}.getType();
			final Map<String, Object> sourceMap = this.gson.fromJson(sourceValue, type);
			final Map<String, Object> targetMap = this.gson.fromJson(targetValue, type);
			final Map<?, ?> sourctSortMap = this.sortMap(sourceMap);
			final Map<?, ?> targetSortMap = this.sortMap(targetMap);
			final String sourceText = this.formatGson.toJson(sourctSortMap);
			final String targetText = this.formatGson.toJson(targetSortMap);
			this.source.setText(sourceText);
			this.target.setText(targetText);
			final String[] sourceLine = Strings.readLine(sourceText);
			final String[] targetLine = Strings.readLine(targetText);
			this.compare(sourceLine, targetLine, this.source);
			this.compare(targetLine, sourceLine, this.target);
			this.source.redraw();
			this.target.redraw();
		});
	};
	
	/**
	 * JSON TO URL
	 */
	private Consumer<MouseEvent> jsonToUrlConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final boolean sort = this.sort.getSelection();
			final boolean encode = this.encode.getSelection();
			final Map<String, String> map;
			final Map<String, String> sourceMap = this.gson.fromJson(sourceValue, new TypeToken<Map<String, String>>() {}.getType());
			if(sort) {
				map = new TreeMap<>();
				map.putAll(sourceMap);
			} else {
				map = sourceMap;
			}
			final String targetValue = MapUtils.toUrl(map, encode);
			this.target.setText(targetValue);
		});
	};
	
	/**
	 * URL TO JSON
	 */
	private Consumer<MouseEvent> urlToJsonConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			final boolean sort = this.sort.getSelection();
			final boolean decode = this.decode.getSelection();
			final Map<String, String> map;
			final Map<String, String> sourceMap = MapUtils.ofUrl(targetValue, decode);
			if(sort) {
				map = new TreeMap<>();
				map.putAll(sourceMap);
			} else {
				map = sourceMap;
			}
			final String sourceValue = this.gson.toJson(map);
			this.source.setText(sourceValue);
		});
	};
	
	/**
	 * 重置
	 */
	private Consumer<MouseEvent> resetConsumer = event -> {
		this.source.setText(Config.EMPTY);
		this.target.setText(Config.EMPTY);
	};
	
	/**
	 * 排序
	 * 
	 * @param source 原始Map
	 * 
	 * @return 排序Map
	 */
	private Map<String, Object> sortMap(Map<?, ?> source) {
		final Map<String, Object> target = new TreeMap<>();
		source.forEach((key, value) -> {
			if (value instanceof List<?> list) {
				target.put((String) key, this.sortList(list));
			} else if (value instanceof Map<?, ?> map) {
				target.put((String) key, this.sortMap(map));
			} else {
				target.put((String) key, value);
			}
		});
		return target;
	}
	
	/**
	 * 排序
	 * 
	 * @param source 原始List
	 * 
	 * @return 排序List
	 */
	private List<?> sortList(List<?> source) {
		final List<Object> target = new ArrayList<>();
		source.forEach(value -> {
			if (value instanceof List<?> list) {
				target.add(this.sortList(list));
			} else if (value instanceof Map<?, ?> map) {
				target.add(this.sortMap(map));
			} else {
				target.add(value);
			}
		});
		target.sort(new Comparator<Object>() {
			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public int compare(Object a, Object b) {
				if (a == null || b == null) {
					return 0;
				}
				if (!a.getClass().equals(b.getClass())) {
					return 0;
				}
				if (a instanceof Comparable && b instanceof Comparable) {
					return ((Comparable) a).compareTo(b);
				}
				return 0;
			}
		});
		return target;
	}
	
	/**
	 * 比较
	 * 
	 * @param sources 原始文本
	 * @param targets 比较文本
	 * @param text 文本输出
	 */
	private void compare(String[] sources, String[] targets, StyledText text) {
		int startIndex = 0;
		for (int index = 0; index < sources.length; index++) {
			final String source = this.trimComma(sources[index]);
			boolean include = false;
			for (int jndex = startIndex; jndex < targets.length; jndex++) {
				final String target = this.trimComma(targets[jndex]);
				if(Objects.equals(source, target)) {
					include = true;
					startIndex = jndex;
					break;
				}
			}
			if(include) {
				text.setLineBackground(index, 1, Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			} else {
				text.setLineBackground(index, 1, Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
			}
		}
	}
	
	/**
	 * 去掉尾部逗号
	 * 
	 * @param value 原始字符串
	 * 
	 * @return 结果字符串
	 */
	private String trimComma(String value) {
		if(value != null && value.endsWith(",")) {
			value = value.substring(0, value.length() - 1);
		}
		return value;
	}
	
}
