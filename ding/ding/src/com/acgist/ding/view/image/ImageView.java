package com.acgist.ding.view.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.acgist.ding.Arrays;
import com.acgist.ding.Config;
import com.acgist.ding.Dialogs;
import com.acgist.ding.DingView;
import com.acgist.ding.Exceptions;
import com.acgist.ding.Internationalization;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码
 * 
 * @author acgist
 */
public class ImageView extends DingView {

	/**
	 * <p>PNG后缀</p>
	 */
	public static final String PNG_SUFFIX = "png";
	/**
	 * JPG后缀</p>
	 */
	public static final String JPG_SUFFIX = "jpg";
	/**
	 * 后缀
	 */
	private static final Map<String, String> SUFFIX_MAPPING = new HashMap<>();
	
	static {
		SUFFIX_MAPPING.put("ff", JPG_SUFFIX);
		SUFFIX_MAPPING.put("89", PNG_SUFFIX);
	}
	
	/**
	 * 原始文本
	 */
	private Text source;
	/**
	 * 图片大小
	 */
	private Combo size;
	/**
	 * 压缩质量
	 */
	private Combo quality;
	/**
	 * 图片输出
	 */
	private Canvas canvas;
	/**
	 * 图片
	 */
	private BufferedImage image;
	/**
	 * 二维码Reader
	 */
	private final QRCodeReader qrCodeReader = new QRCodeReader();
	/**
	 * 二维码Writer
	 */
	private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

	@Override
	public void createPartControl(Composite composite) {
		this.layout(composite, SWT.HORIZONTAL);
		// 原码模块
		final Composite sourceGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.source = this.buildMultiText(sourceGroup, Internationalization.Message.IMAGE_SOURCE);
		// 操作模块
		final Group operationGroup = this.buildGroup(composite, Internationalization.Label.OPERATION, 2, 0, SWT.VERTICAL);
		// 二维码模块
		final Group qrCodeGroup = this.buildGroup(operationGroup, Internationalization.Label.QRCODE, 0, 0, SWT.VERTICAL);
		final Composite qrCodeConfigGroup = this.buildRowComposite(qrCodeGroup, SWT.HORIZONTAL);
		this.size = this.buildSelect(qrCodeConfigGroup, Internationalization.Label.SIZE, 1, "128", "256", "512");
		final Composite qrCodeButtonGroup = this.buildRowComposite(qrCodeGroup, SWT.HORIZONTAL);
		this.buildButton(qrCodeButtonGroup, Internationalization.Button.BUILD, this.buildConsumer);
		this.buildButton(qrCodeButtonGroup, Internationalization.Button.COPY, this.copyConsumer);
		this.buildButton(qrCodeButtonGroup, Internationalization.Button.FILE_IMPORT, this.importConsumer);
		this.buildButton(qrCodeButtonGroup, Internationalization.Button.FILE_EXPORT, this.exportConsumer);
		// 图片模块
		final Group imageGroup = this.buildGroup(operationGroup, Internationalization.Label.IMAGE, 0, 0, SWT.VERTICAL);
		final Composite imageConfigGroup = this.buildRowComposite(imageGroup, SWT.HORIZONTAL);
		this.quality = this.buildSelect(imageConfigGroup, Internationalization.Label.QUALITY, 0, "0.8", "0.6", "0.4");
		final Composite imageButtonGroup = this.buildRowComposite(imageGroup, SWT.HORIZONTAL);
		this.buildButton(imageButtonGroup, Internationalization.Button.COMPRESS, this.compressConsumer);
		// 二级操作模块
		final Composite subOperationGroup = this.buildGroup(operationGroup, Internationalization.Label.OPERATION, 0, 0, SWT.HORIZONTAL);
		this.buildButton(subOperationGroup, Internationalization.Button.RESET, this.resetConsumer);
		// 图片输出模块
		final Composite targetGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.canvas = new Canvas(targetGroup, SWT.BORDER);
		this.canvas.addPaintListener(this.paintListener);
		// 图片导入
		final DropTarget dropTarget = new DropTarget(composite, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
		dropTarget.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dropTarget.addDropListener(this.dropTargetAdapter);
	}

	/**
	 * 生成
	 */
	private Consumer<MouseEvent> buildConsumer = event -> {
		this.consumer(this.source, this::redraw);
	};

	/**
	 * 复制
	 */
	private Consumer<MouseEvent> copyConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final BitMatrix bitMatrix = this.redraw(sourceValue);
			final Clipboard clipboard = new Clipboard(Display.getDefault());
			final BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			clipboard.setContents(new ImageData[] { this.convert(image) }, new ImageTransfer[] { ImageTransfer.getInstance() });
		});
	};

	/**
	 * 导入
	 */
	private Consumer<MouseEvent> importConsumer = event -> {
		final String file = Dialogs.file("*.jpg;*.png;*.gif", "*.*");
		if(file == null) {
			return;
		}
		this.importFile(file);
	};
	
	/**
	 * 导出
	 */
	private Consumer<MouseEvent> exportConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final BitMatrix bitMatrix = this.redraw(sourceValue);
			final String folder = Dialogs.folder();
			if (folder == null) {
				return;
			}
			try {
				MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(folder, System.currentTimeMillis() + ".png"));
			} catch (IOException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};

	/**
	 * 压缩
	 */
	private Consumer<MouseEvent> compressConsumer = event -> {
		// 压缩文件
		final String[] files = Dialogs.files("*.jpg;*.png");
		if(files == null) {
			return;
		}
		// 导出目录
		final String folder = Dialogs.folder();
		if(folder == null) {
			return;
		}
		this.source.setText(Config.EMPTY);
		for (String file : files) {
			this.source.append(file);
			this.source.append(" - ");
			this.compress(file, folder);
			this.source.append(Config.NEW_LINE);
		}
	};
	
	/**
	 * 重置
	 */
	private Consumer<MouseEvent> resetConsumer = event -> {
		this.source.setText(Config.EMPTY);
	};
	
	/**
	 * 重画
	 */
	private PaintListener paintListener = event -> {
		final GC gc = event.gc;
		if (this.image == null || gc.isDisposed()) {
			return;
		}
		final int width = this.canvas.getBounds().width;
		final int height = this.canvas.getBounds().height;
		final int imageWidth = this.image.getWidth();
		final int imageHeight = this.image.getHeight();
		final int x = width > imageWidth ? ((width - imageHeight) / 2) : 0;
		final int y = height > imageHeight ? ((height - imageHeight) / 2) : 0;
		final Image drawImage = new Image(Display.getDefault(), this.convert(this.image));
		gc.drawImage(drawImage, x, y);
		drawImage.dispose();
	};

	/**
	 * 拖拽
	 */
	private DropTargetAdapter dropTargetAdapter = new DropTargetAdapter() {
		@Override
		public void dragEnter(DropTargetEvent event) {
			if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		};
		@Override
		public void drop(DropTargetEvent event) {
			if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
				final String[] files = (String[]) event.data;
				if(Arrays.isNotEmpty(files)) {
					// 默认选择首个文件
					ImageView.this.importFile(files[0]);
				}
			}
		}
	};
	
	/**
	 * 画图
	 * 
	 * @return 二维码信息
	 */
	private BitMatrix redraw(String sourceValue) {
		final BitMatrix bitMatrix = this.build(sourceValue);
		this.image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		this.canvas.redraw();
		return bitMatrix;
	}
	
	/**
	 * 导入文件
	 * 
	 * @param file 文件路径
	 */
	private void importFile(String file) {
		try {
			final BufferedImage bufferedImage = ImageIO.read(Paths.get(file).toFile());
			final BinaryBitmap binaryBitmap= new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
			final String sourceValue = this.qrCodeReader.decode(binaryBitmap).toString();
			this.source.setText(sourceValue);
		} catch (IOException | NotFoundException | ChecksumException | FormatException e) {
			Exceptions.error(this.getClass(), e);
		}
	}
	
	/**
	 * 生成二维码
	 * 
	 * @param value 文本
	 * 
	 * @return 二维码信息
	 */
	private BitMatrix build(String value) {
		final Map<EncodeHintType, Object> hintMap = new HashMap<>();
		hintMap.put(EncodeHintType.MARGIN, 2);
		hintMap.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		final int width = Integer.parseInt(this.size.getText());
		try {
			return this.qrCodeWriter.encode(value, BarcodeFormat.QR_CODE, width, width, hintMap);
		} catch (WriterException e) {
			Exceptions.error(this.getClass(), e);
		}
		return null;
	}
	
	/**
	 * 图片转换
	 * 
	 * @param bufferedImage 图片信息
	 * 
	 * @return 图片数据
	 */
	private ImageData convert(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel colorModel) {
			final PaletteData paletteData = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			final ImageData imageData = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), paletteData);
			for (int y = 0; y < imageData.height; y++) {
				for (int x = 0; x < imageData.width; x++) {
					final int rgb = bufferedImage.getRGB(x, y);
					final int pixel = paletteData.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
					imageData.setPixel(x, y, pixel);
					if (colorModel.hasAlpha()) {
						imageData.setAlpha(x, y, (rgb >> 24) & 0xFF);
					}
				}
			}
			return imageData;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel colorModel) {
			final int size = colorModel.getMapSize();
			final byte[] red = new byte[size];
			final byte[] green = new byte[size];
			final byte[] blue = new byte[size];
			colorModel.getReds(red);
			colorModel.getGreens(green);
			colorModel.getBlues(blue);
			final RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(red[i] & 0xFF, green[i] & 0xFF, blue[i] & 0xFF);
			}
			final PaletteData paletteData = new PaletteData(rgbs);
			final ImageData imageData = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), paletteData);
			imageData.transparentPixel = colorModel.getTransparentPixel();
			final WritableRaster writableRaster = bufferedImage.getRaster();
			final int[] pixels = new int[1];
			for (int y = 0; y < imageData.height; y++) {
				for (int x = 0; x < imageData.width; x++) {
					writableRaster.getPixel(x, y, pixels);
					imageData.setPixel(x, y, pixels[0]);
				}
			}
			return imageData;
		} else if (bufferedImage.getColorModel() instanceof ComponentColorModel colorModel) {
			final PaletteData paletteData = new PaletteData(0x0000FF, 0x00FF00, 0xFF0000);
			final ImageData imageData = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), paletteData);
			imageData.transparentPixel = -1;
			final WritableRaster writableRaster = bufferedImage.getRaster();
			final int[] pixels = new int[3];
			for (int y = 0; y < imageData.height; y++) {
				for (int x = 0; x < imageData.width; x++) {
					writableRaster.getPixel(x, y, pixels);
					imageData.setPixel(x, y, paletteData.getPixel(new RGB(pixels[0], pixels[1], pixels[2])));
				}
			}
			return imageData;
		}
		return null;
	}
	
	/**
	 * <p>压缩图片</p>
	 * 
	 * @param imagePath 图片地址
	 * @param folder 输出目录
	 */
	private void compress(String imagePath, String folder) {
		final File file = new File(imagePath);
		if(!file.exists() || !file.isFile()) {
			this.source.append(Internationalization.get(Internationalization.Message.DATA_EMPTY));
			return;
		}
		final String outputPath = Paths.get(folder, file.getName()).toString();
		final String quality = this.quality.getText();
		try (
			final OutputStream output = new FileOutputStream(outputPath);
		) {
			boolean success = true;
			final String suffix = this.readSuffix(imagePath);
			final ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(suffix).next();
			imageWriter.setOutput(ImageIO.createImageOutputStream(output));
			final BufferedImage sourceBufferedImage = ImageIO.read(file);
			if(PNG_SUFFIX.equals(suffix.toLowerCase())) {
				final int width = sourceBufferedImage.getWidth();
				final int height = sourceBufferedImage.getHeight();
				final int intQuality = switch(quality) {
				case "0.8" -> BufferedImage.TYPE_USHORT_565_RGB;
				case "0.6" -> BufferedImage.TYPE_USHORT_555_RGB;
				case "0.4" -> BufferedImage.TYPE_BYTE_INDEXED;
				default -> BufferedImage.TYPE_USHORT_565_RGB;
				};
				final BufferedImage targetBufferedImage = new BufferedImage(width, height, intQuality);
				final Graphics2D graphics2D = targetBufferedImage.createGraphics();
				graphics2D.setBackground(Color.WHITE);
				graphics2D.clearRect(0, 0, width, height);
				graphics2D.drawImage(sourceBufferedImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
				imageWriter.write(new IIOImage(targetBufferedImage, null, null));
			} else if(JPG_SUFFIX.equals(suffix.toLowerCase())) {
				final ImageWriteParam imageWriteParam = new JPEGImageWriteParam(null);
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality(Float.parseFloat(quality));
				imageWriteParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
				final ColorModel colorModel = sourceBufferedImage.getColorModel();
				imageWriteParam.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
				imageWriter.write(null, new IIOImage(sourceBufferedImage, null, null), imageWriteParam);
			} else {
				success = false;
			}
			output.flush();
			if(success) {
				this.source.append(Internationalization.get(Internationalization.Message.SUCCESS));
			} else {
				this.source.append(Internationalization.get(Internationalization.Message.DATA_NONSUPPORT));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Exceptions.error(this.getClass(), e);
		}
	}
	
	/**
	 * <p>识别图片格式</p>
	 * 
	 * @param path 图片路径
	 * 
	 * @return 格式后缀
	 * 
	 * @throws IOException IO异常
	 */
	private String readSuffix(String path) throws IOException {
		try (InputStream inputStream = new FileInputStream(new File(path));) {
			final byte[] bytes = new byte[1];
			inputStream.read(bytes);
			final String type = Integer.toHexString(bytes[0] & 0xFF).toLowerCase();
			return SUFFIX_MAPPING.get(type);
		}
	}

}
