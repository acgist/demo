package com.acgist.ding.view.common;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.acgist.ding.Config;
import com.acgist.ding.Dialogs;
import com.acgist.ding.DingView;
import com.acgist.ding.Exceptions;
import com.acgist.ding.Internationalization;

/**
 * 通用
 * 
 * @author acgist
 */
public class CommonView extends DingView {

	/**
	 * 原始文本
	 */
	private Text source;
	/**
	 * 目标文本
	 */
	private Text target;
	/**
	 * 休息时间
	 */
	private Combo restTime;
	/**
	 * 结果文本
	 */
	private Text result;
	/**
	 * Control Key Code
	 */
	private static final int CONTROL = 0x40000;
	/**
	 * 休息定时任务
	 */
	private static ScheduledFuture<?> restScheduledFuture;
	
	@Override
	public void createPartControl(Composite composite) {
		this.layout(composite, SWT.VERTICAL);
		// 文本模块
		final Composite sourceTargetGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.source = this.buildMultiText(sourceTargetGroup, Internationalization.Message.COMMON_SOURCE);
		this.target = this.buildMultiText(sourceTargetGroup, Internationalization.Message.COMMON_TARGET);
		// 操作模块
		final Group operationGroup = this.buildGroup(composite, Internationalization.Label.OPERATION, 0, 0, SWT.VERTICAL);
		// 颜色模块
		final Composite colorGroup = this.buildGroup(operationGroup, Internationalization.Label.COLOR, 0, 0, SWT.HORIZONTAL);
		this.buildButton(colorGroup, Internationalization.Button.COLOR, this.colorConsumer);
		this.buildButton(colorGroup, Internationalization.Button.COLOR_HEX_RGB, this.colorHexRgbConsumer);
		this.buildButton(colorGroup, Internationalization.Button.COLOR_RGB_HEX, this.colorRgbHexConsumer);
		// 休息模块
		final Composite restGroup = this.buildGroup(operationGroup, Internationalization.Label.REST, 0, 0, SWT.HORIZONTAL);
		this.restTime = this.buildSelect(restGroup, Internationalization.Label.REST_TIME, 2, "30S", "15M", "30M", "45M", "1H");
		if(restScheduledFuture == null) {
			this.buildButton(restGroup, Internationalization.Button.REST, this.restConsumer);
		} else {
			this.buildButton(restGroup, Internationalization.Button.REST_CANCEL, this.restConsumer);
		}
		// 配置模块
		final Composite configGroup = this.buildGroup(operationGroup, Internationalization.Label.CONFIG, 0, 0, SWT.HORIZONTAL);
		this.buildButton(configGroup, Internationalization.Button.FONT, this.fontConsumer);
		this.buildButton(configGroup, Internationalization.Button.ENCODING, this.encodingConsumer);
		this.buildButton(configGroup, Internationalization.Button.EYESHIELD, this.eyeshieldConsumer);
		// 正则表达式模块
		final Composite regexGroup = this.buildGroup(operationGroup, Internationalization.Label.REGEX, 0, 0, SWT.HORIZONTAL);
		this.buildButton(regexGroup, Internationalization.Button.REGEX_FIND, this.regexFindConsumer);
		this.buildButton(regexGroup, Internationalization.Button.REGEX_MATCH, this.regexMatchConsumer);
		// 二级操作模块
		final Composite subOperationGroup = this.buildGroup(operationGroup, Internationalization.Label.OPERATION, 0, 0, SWT.HORIZONTAL);
		this.buildButton(subOperationGroup, Internationalization.Button.RESET, this.resetConsumer);
		// 结果模块
		final Composite resultGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.result = this.buildMultiText(resultGroup, Internationalization.Message.COMMON_RESULT);
	}

	/**
	 * 取色器
	 * 
	 * 鼠标移动取色位置单击Ctrl按键即可
	 * 
	 * 注意：不可失去焦点
	 */
	private Consumer<MouseEvent> colorConsumer = event -> {
		final Button button = (Button) event.getSource();
		if(Internationalization.get(Internationalization.Button.COLOR).equals(button.getText())) {
			button.setText(Internationalization.get(Internationalization.Button.COLOR_OFF));
			Display.getDefault().addFilter(SWT.KeyDown, this.keyListener);
		} else {
			button.setText(Internationalization.get(Internationalization.Button.COLOR));
			Display.getDefault().removeFilter(SWT.KeyDown, this.keyListener);
		}
	};
	
	/**
	 * 取色器按键
	 */
	private Listener keyListener = keyEvent -> {
		if(keyEvent.keyCode == CONTROL) {
			try {
				final Robot robot = new Robot();
				final Point point = MouseInfo.getPointerInfo().getLocation();
				final Color color = robot.getPixelColor(point.x, point.y);
				this.source.setText(String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
			} catch (AWTException e) {
				Exceptions.error(this.getClass(), e);
			}
		}
	};
	
	/**
	 * 颜色转换：Hex To RGB
	 */
	private Consumer<MouseEvent> colorHexRgbConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			if(sourceValue.startsWith("#")) {
				sourceValue = sourceValue.substring(1);
			}
			int red = 0;
			int green = 0;
			int blue = 0;
			if(sourceValue.length() == 3) {
				red = Integer.parseInt(sourceValue.substring(0, 1).repeat(2), 16);
				green = Integer.parseInt(sourceValue.substring(1, 2).repeat(2), 16);
				blue = Integer.parseInt(sourceValue.substring(2, 3).repeat(2), 16);
				this.target.setText(String.format("(%d, %d, %d)", red, green, blue));
			} else if(sourceValue.length() == 6) {
				red = Integer.parseInt(sourceValue.substring(0, 2), 16);
				green = Integer.parseInt(sourceValue.substring(2, 4), 16);
				blue = Integer.parseInt(sourceValue.substring(4, 6), 16);
				this.target.setText(String.format("(%d, %d, %d)", red, green, blue));
			}
		});
	};
	
	/**
	 * 颜色转换：RGB To Hex
	 */
	private Consumer<MouseEvent> colorRgbHexConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			if(targetValue.startsWith("(")) {
				targetValue = targetValue.substring(1);
			}
			if(targetValue.endsWith(")")) {
				targetValue = targetValue.substring(0, targetValue.length() - 1);
			}
			final String[] rgbs = targetValue.split(",");
			if(rgbs.length == 3 || rgbs.length == 4) {
				final int red = Integer.parseInt(rgbs[0].trim());
				final int green = Integer.parseInt(rgbs[1].trim());
				final int blue = Integer.parseInt(rgbs[2].trim());
				this.source.setText(String.format("#%02X%02X%02X", red, green, blue));
			}
		});
	};
	
	/**
	 * 定时休息
	 */
	private Consumer<MouseEvent> restConsumer = event -> {
		final Button button = (Button) event.getSource();
		if(restScheduledFuture == null) {
			final int restTime = this.restTime();
			restScheduledFuture = Config.SCHEDULED.scheduleAtFixedRate(() -> {
				Display.getDefault().syncExec(() -> Dialogs.info(Internationalization.Message.REST));
			}, restTime, restTime, TimeUnit.SECONDS);
			button.setText(Internationalization.get(Internationalization.Button.REST_CANCEL));
		} else {
			restScheduledFuture.cancel(true);
			restScheduledFuture = null;
			button.setText(Internationalization.get(Internationalization.Button.REST));
		}
	};
	
	/**
	 * 休息时间
	 * 
	 * @return 休息时间
	 */
	private int restTime() {
		return switch (this.restTime.getText()) {
		case "30S" -> 30;
		case "15M" -> 15 * 60;
		case "30M" -> 30 * 60;
		case "45M" -> 45 * 60;
		case "1H" -> 60 * 60;
		default -> 10;
		};
	}
	
	/**
	 * 字体调整
	 */
	private Consumer<MouseEvent> fontConsumer = event -> {
//		final IPreferencesService preferencesService = Platform.getPreferencesService();
//		final Preferences preferences = preferencesService.getRootNode();
//		final String[] keys = preferences.childrenNames();
//		final String[] keys = preferences.keys();
		// TODO：实现
	};
	
	/**
	 * 编码调整
	 */
	private Consumer<MouseEvent> encodingConsumer = event -> {
		// TODO：实现
	};
	
	/**
	 * 护眼模式
	 */
	private Consumer<MouseEvent> eyeshieldConsumer = event -> {
		this.result.setText("202,232,202");
		// TODO：实现
	};
	
	/**
	 * 正则表达式提取
	 */
	private Consumer<MouseEvent> regexFindConsumer = event -> {
		this.consumer(this.source, this.target, (sourceValue, targetValue) -> {
			this.result.setText(Config.EMPTY);
			final Pattern pattern = Pattern.compile(targetValue);
			final Matcher matcher = pattern.matcher(sourceValue);
			while(matcher.find()) {
				final int count = matcher.groupCount();
				for (int index = 1; index <= count; index++) {
					this.result.append(matcher.group(index));
					this.result.append(Config.NEW_LINE);
				}
			}
		});
	};
	
	/**
	 * 正则表达式匹配
	 */
	private Consumer<MouseEvent> regexMatchConsumer = event -> {
		this.consumer(this.source, this.target, (sourceValue, targetValue) -> {
			if(sourceValue.matches(targetValue)) {
				Dialogs.info(Internationalization.Message.SUCCESS);
			} else {
				Dialogs.info(Internationalization.Message.FAIL);
			}
		});
	};
	
	/**
	 * 重置
	 */
	private Consumer<MouseEvent> resetConsumer = event -> {
		this.source.setText(Config.EMPTY);
		this.target.setText(Config.EMPTY);
		this.result.setText(Config.EMPTY);
	};
	
}
