package com.acgist.ding;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * View
 * 
 * @author acgist
 */
public abstract class DingView extends ViewPart {

	@Override
	public void setFocus() {
	}
	
	/**
	 * 设置Layout
	 * 
	 * @param composite 组件
	 * 
	 * @param style 样式
	 */
	protected void layout(Composite composite, int style) {
		composite.setLayout(this.fullLayout(4, 4, style));
	}
	
	/**
	 * 设置RowLayout
	 * 
	 * @param width 宽度距离
	 * @param height 高度距离
	 * @param style 样式
	 * 
	 * @return RowLayout
	 */
	protected RowLayout rowLayout(int width, int height, int style) {
		final RowLayout layout = new RowLayout(style);
		layout.marginTop = height;
		layout.marginLeft = width;
		layout.marginRight = width;
		layout.marginBottom = height;
		return layout;
	}
	
	/**
	 * 设置FillLayout
	 * 
	 * @param width 宽度距离
	 * @param height 高度距离
	 * @param style 样式
	 * 
	 * @return FillLayout
	 */
	protected FillLayout fullLayout(int width, int height, int style) {
		final FillLayout layout = new FillLayout(style);
		layout.marginWidth = width;
		layout.marginHeight = height;
		return layout;
	}
	
	/**
	 * 消费
	 * 
	 * @param text 生产者
	 * @param consumer 消费者
	 */
	protected void consumer(Text text, Consumer<String> consumer) {
		final String value = text.getText().trim();
		if (Strings.isEmpty(value)) {
			Dialogs.info(Internationalization.Message.DATA_EMPTY);
		} else {
			consumer.accept(value);
		}
	}
	
	/**
	 * 消费
	 * 
	 * @param source 生产者
	 * @param target 生产者
	 * @param consumer 消费者
	 */
	protected void consumer(Text source, Text target, BiConsumer<String, String> consumer) {
		final String sourceValue = source.getText().trim();
		final String targetValue = target.getText().trim();
		if (Strings.isEmpty(sourceValue) || Strings.isEmpty(targetValue)) {
			Dialogs.info(Internationalization.Message.DATA_EMPTY);
		} else {
			consumer.accept(sourceValue, targetValue);
		}
	}
	
	/**
	 * 消费
	 * 
	 * @param text 生产者
	 * @param consumer 消费者
	 */
	protected void consumer(StyledText text, Consumer<String> consumer) {
		final String value = text.getText().trim();
		if (Strings.isEmpty(value)) {
			Dialogs.info(Internationalization.Message.DATA_EMPTY);
		} else {
			consumer.accept(value);
		}
	}
	
	/**
	 * 消费
	 * 
	 * @param source 生产者
	 * @param target 生产者
	 * @param consumer 消费者
	 */
	protected void consumer(StyledText source, StyledText target, BiConsumer<String, String> consumer) {
		final String sourceValue = source.getText().trim();
		final String targetValue = target.getText().trim();
		if (Strings.isEmpty(sourceValue) || Strings.isEmpty(targetValue)) {
			Dialogs.info(Internationalization.Message.DATA_EMPTY);
		} else {
			consumer.accept(sourceValue, targetValue);
		}
	}
	
	/**
	 * 创建文本框
	 * 
	 * @param composite 组件
	 * @param name I18N名称
	 * 
	 * @return 文本框
	 */
	protected Text buildText(Composite composite, String name) {
		final Composite textComposite = new Composite(composite, SWT.NONE);
		textComposite.setLayout(this.rowLayout(0, 0, SWT.HORIZONTAL));
		final CLabel label = new CLabel(textComposite, SWT.CENTER);
		label.setText(Internationalization.get(name));
		final Text text = new Text(textComposite, SWT.BORDER);
		return text;
	}
	
	/**
	 * 创建文本框
	 * 
	 * @param composite 组件
	 * @param name I18N名称
	 * 
	 * @return 文本框
	 */
	protected Text buildMultiText(Composite composite, String name) {
		final Text text = new Text(composite, SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		text.setToolTipText(Internationalization.get(name));
		return text;
	}
	
	/**
	 * 创建文本框
	 * 
	 * @param composite 组件
	 * @param name I18N名称
	 * 
	 * @return 文本框
	 */
	protected StyledText buildMultiStyledText(Composite composite, String name) {
		final StyledText styledText = new StyledText(composite, SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		styledText.setToolTipText(Internationalization.get(name));
		return styledText;
	}
	
	/**
	 * 创建组
	 * 
	 * @param composite 组件
	 * @param name I18N名称
	 * @param width 宽度距离
	 * @param height 高度距离
	 * @param style 样式
	 * 
	 * @return 组
	 */
	protected Group buildGroup(Composite composite, String name, int width, int height, int style) {
		final Composite groupComposite = new Composite(composite, SWT.NONE);
		groupComposite.setLayout(this.fullLayout(width, height, style));
		final Group group = new Group(groupComposite, SWT.NONE);
		group.setText(Internationalization.get(name));
		group.setLayout(this.rowLayout(0, 0, style));
		return group;
	}
	
	/**
	 * 创建单选框
	 * 
	 * @param composite 组件
	 * @param name I18N名称
	 * 
	 * @return 单选框
	 */
	protected Button buildCheck(Composite composite, String name) {
		final Button button = new Button(composite, SWT.CHECK);
		button.setText(Internationalization.get(name));
		return button;
	}
	
	/**
	 * 创建复选框
	 * 
	 * @param composite 组件
	 * @param name I18N名称
	 * @param defaultIndex 默认选择
	 * @param values 选项
	 * 
	 * @return 复选框
	 */
	protected Combo buildSelect(Composite composite, String name, int defaultIndex, String ... values) {
		final Composite selectComposite = new Composite(composite, SWT.NONE);
		selectComposite.setLayout(this.rowLayout(0, 0, SWT.HORIZONTAL));
		final CLabel label = new CLabel(selectComposite, SWT.CENTER);
		label.setText(Internationalization.get(name));
		final Combo select = new Combo(selectComposite, SWT.DROP_DOWN);
		select.setItems(values);
		select.setText(Internationalization.get(name));
		select.select(defaultIndex);
		return select;
	}
	
	/**
	 * 创建按钮
	 * 
	 * @param composite 组件
	 * @param name I18N名称
	 * @param consumer 消费者
	 * 
	 * @return 按钮
	 */
	protected Button buildButton(Composite composite, String name, Consumer<MouseEvent> consumer) {
		final Button button = new Button(composite, SWT.NONE);
		button.setText(Internationalization.get(name));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				consumer.accept(event);
			}
		});
		return button;
	}
	
	/**
	 * 创建组件
	 * 
	 * @param composite 组件
	 * @param style 样式
	 * 
	 * @return 组件
	 */
	protected Composite buildRowComposite(Composite composite, int style) {
		final Composite group = new Composite(composite, SWT.NONE);
		group.setLayout(this.rowLayout(0, 0, style));
		return group;
	}
	
	/**
	 * 创建组件
	 * 
	 * @param composite 组件
	 * @param style 样式
	 * 
	 * @return 组件
	 */
	protected Composite buildFullComposite(Composite composite, int style) {
		final Composite group = new Composite(composite, SWT.NONE);
		group.setLayout(this.fullLayout(0, 0, style));
		return group;
	}
	
}
