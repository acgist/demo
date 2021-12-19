package com.acgist.ding.view.codec;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.acgist.ding.Config;
import com.acgist.ding.Dialogs;
import com.acgist.ding.DingView;
import com.acgist.ding.Exceptions;
import com.acgist.ding.Internationalization;
import com.acgist.ding.Strings;

/**
 * 编码解码
 * 
 * @author acgist
 */
public class CodecView extends DingView {

	/**
	 * 原始文本
	 */
	private Text source;
	/**
	 * 编码文本
	 */
	private Text target;
	
	@Override
	public void createPartControl(Composite composite) {
		this.layout(composite, SWT.VERTICAL);
		// 原始文本
		final Composite sourceGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.source = this.buildMultiText(sourceGroup, Internationalization.Message.CODEC_SOURCE);
		// 操作模块
		final Group operationGroup = this.buildGroup(composite, Internationalization.Label.OPERATION, 0, 0, SWT.VERTICAL);
		final Composite buttonGroup = this.buildRowComposite(operationGroup, SWT.HORIZONTAL);
		this.buildButton(buttonGroup, Internationalization.Button.HEX_ENCODE, this.hexEncodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.HEX_DECODE, this.hexDecodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.URL_ENCODE, this.urlEncodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.URL_DECODE, this.urlDecodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.BASE64_ENCODE, this.base64EncodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.BASE64_DECODE, this.base64DecodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.FILE_IMPORT, this.base64FileEncodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.FILE_EXPORT, this.base64FileDecodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.UNICODE_ENCODE, this.unicodeEncodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.UNICODE_DECODE, this.unicodeDecodeConsumer);
		this.buildButton(buttonGroup, Internationalization.Button.RESET, this.resetConsumer);
		// 编码文本
		final Composite targetGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.target = this.buildMultiText(targetGroup, Internationalization.Message.CODEC_TARGET);
	}
	
	/**
	 * Hex编码
	 */
	private Consumer<MouseEvent> hexEncodeConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final String targetValue = Strings.hex(sourceValue.getBytes());
			this.target.setText(targetValue);
		});
	};
	
	/**
	 * Hex解码
	 */
	private Consumer<MouseEvent> hexDecodeConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			final String sourceValue = new String(Strings.unhex(targetValue));
			this.source.setText(sourceValue);
		});
	};
	
	/**
	 * URL编码
	 */
	private Consumer<MouseEvent> urlEncodeConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final String targetValue = URLEncoder.encode(sourceValue, StandardCharsets.UTF_8);
			this.target.setText(targetValue);
		});
	};
	
	/**
	 * URL解码
	 */
	private Consumer<MouseEvent> urlDecodeConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			final String sourceValue = URLDecoder.decode(targetValue, StandardCharsets.UTF_8);
			this.source.setText(sourceValue);
		});
	};
	
	/**
	 * Base64编码
	 */
	private Consumer<MouseEvent> base64EncodeConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final String targetValue = Base64.getMimeEncoder().encodeToString(sourceValue.getBytes());
			this.target.setText(targetValue);
		});
	};
	
	/**
	 * Base64解码
	 */
	private Consumer<MouseEvent> base64DecodeConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			final String sourceValue = new String(Base64.getMimeDecoder().decode(targetValue));
			this.source.setText(sourceValue);
		});
	};
	
	/**
	 * Base64文件编码
	 */
	private Consumer<MouseEvent> base64FileEncodeConsumer = event -> {
		final String file = Dialogs.file("*.*");
		if (file == null) {
			return;
		}
		this.source.setText(file);
		try {
			final byte[] bytes = Files.readAllBytes(Paths.get(file));
			final String targetValue = Base64.getMimeEncoder().encodeToString(bytes);
			this.target.setText(targetValue);
		} catch (IOException e) {
			Exceptions.error(this.getClass(), e);
		}
	};
	
	/**
	 * Base64文件解码
	 */
	private Consumer<MouseEvent> base64FileDecodeConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			final String file = Dialogs.file("*.*");
			if (file == null) {
				return;
			}
			this.source.setText(file);
			final byte[] bytes = Base64.getMimeDecoder().decode(targetValue);
			try {
				Files.write(Paths.get(file), bytes);
			} catch (IOException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * Unicode编码
	 */
	private Consumer<MouseEvent> unicodeEncodeConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final String targetValue = Strings.toUnicode(sourceValue);
			this.target.setText(targetValue);
		});
	};
	
	/**
	 * Unicode解码
	 */
	private Consumer<MouseEvent> unicodeDecodeConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			final String sourceValue = Strings.ofUnicode(targetValue);
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
	
}
