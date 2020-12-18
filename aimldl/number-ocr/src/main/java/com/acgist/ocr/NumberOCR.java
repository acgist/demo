package com.acgist.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.datavec.api.io.labels.PathLabelGenerator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Text;
import org.datavec.api.writable.Writable;
import org.datavec.image.data.ImageWritable;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模型使用
 */
public class NumberOCR {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOCR.class);

	public static void main(String[] args) throws IOException {
		MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(new File("./number.model"));
//		DataSetIterator testData = new MnistDataSetIterator(2, false, 12345);
//		DataSet set = testData.next();
//		DataSetIterator testData = readOri(); // 手工处理的数据
		DataSetIterator testData = readProcess(); // 自动处理数据
		DataSet set = testData.next();
		set.getFeatureMatrix().divi(0.70F);
		LOGGER.debug("测试数据：{}", set.getFeatureMatrix());
		int[] data = model.predict(set.getFeatureMatrix());
//		LOGGER.info("获取最大值索引：{}", Nd4j.getBlasWrapper().iamax(model.output(set.getFeatureMatrix()).getRow(0)));
		LOGGER.info("labels：{}", set.getLabels());
		for (int i = 0; i < data.length; i++) {
			LOGGER.info("识别结果：{}", data[i]);
		}
	}
	
	/**
	 * 原始图片
	 * t-3、t-5是处理过的能正确识别，f-3、f-5没有处理过不能正确识别
	 */
	public static final DataSetIterator readOri() {
//		try(RecordReader reader = new ImageRecordReader()) {
		try(RecordReader reader = new ImageRecordReader(28, 28, 1, new PathLabelGenerator() { // 生成label
			private static final long serialVersionUID = 1L;
			@Override
			public Writable getLabelForPath(URI uri) {
				return getLabelForPath(uri.toString());
			}
			@Override
			public Writable getLabelForPath(String path) {
				String name = FilenameUtils.getBaseName(path);
				int index = name.lastIndexOf("-") + 1;
				return new Text(name.substring(index, index + 1));
			}
		})) {
			String path = NumberOCR.class.getResource("/number/readOri/").getFile();
			LOGGER.info("识别图片路径：{}", path);
			reader.initialize(new FileSplit(new File(path)));
			DataSetIterator dataSet = new RecordReaderDataSetIterator(reader, 10);
			dataSet.setPreProcessor(new ImagePreProcessingScaler(0, 1)); // 归一化
			return dataSet;
		} catch (IOException | InterruptedException e) {
			LOGGER.error("图片加载异常", e);
		}
		return null;
	}
	
	/**
	 * 添加图片处理：二值化
	 */
	public static final DataSetIterator readProcess() {
//		try(RecordReader reader = new ImageRecordReader()) {
		try(RecordReader reader = new ImageRecordReader(28, 28, 1, new PathLabelGenerator() { // 生成label
			private static final long serialVersionUID = 1L;
			@Override
			public Writable getLabelForPath(URI uri) {
				return getLabelForPath(uri.toString());
			}
			@Override
			public Writable getLabelForPath(String path) {
				String name = FilenameUtils.getBaseName(path);
				int index = name.lastIndexOf("-") + 1;
				return new Text(name.substring(index, index + 1));
			}
		}, new ImageTransform() { // 图片处理
			@Override
			public ImageWritable transform(ImageWritable image, Random random) {
		        if (image == null) {
		            return null;
		        }
		        Java2DFrameConverter converter = new Java2DFrameConverter();
		        BufferedImage bufferedImage = converter.convert(image.getFrame());
//		        bufferedImage = ImageTool.gray(bufferedImage);
		        bufferedImage = ImageTool.binary(bufferedImage);
		        bufferedImage = ImageTool.inverse(bufferedImage);
		        bufferedImage = ImageTool.resize(bufferedImage, 28, 28);
//		        try {
//		        	ImageIO.write(bufferedImage, "jpg", new File("e:/tmp/number/" + UUID.randomUUID().toString() + ".jpg"));
//		        } catch (IOException e) {
//		        	e.printStackTrace();
//		        }
				return new ImageWritable(converter.convert(bufferedImage));
			}
			@Override
			public ImageWritable transform(ImageWritable image) {
				return this.transform(image, new Random());
			}
		})) {
//			String path = NumberOCR.class.getResource("/number/test/").getFile();
			String path = NumberOCR.class.getResource("/number/readProcess/").getFile();
			LOGGER.info("识别图片路径：{}", path);
			reader.initialize(new FileSplit(new File(path)));
			DataSetIterator dataSet = new RecordReaderDataSetIterator(reader, 10);
			dataSet.setPreProcessor(new ImagePreProcessingScaler(0, 1)); // 归一化
			return dataSet;
		} catch (IOException | InterruptedException e) {
			LOGGER.error("图片加载异常", e);
		}
		return null;
	}

}
