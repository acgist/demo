package com.acgist.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.ocr.ImageTool;

@RestController
@RequestMapping("/number")
public class NumberController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NumberController.class);
	
	private MultiLayerNetwork model;
	
	@PostConstruct
	private void init() {
		try {
			model = ModelSerializer.restoreMultiLayerNetwork(new File("./number.model"));
		} catch (IOException e) {
			LOGGER.error("模型初始化异常", e);
		}
	}
	
	@RequestMapping
	public String index(String data) throws IOException {
		return predict(data);
	}
	
	public String predict(String image) throws IOException {
		DataSetIterator testData = readProcess(image);
		DataSet set = testData.next();
		LOGGER.debug("测试数据：{}", set.getFeatureMatrix());
		int[] data = model.predict(set.getFeatureMatrix());
		LOGGER.debug("labels：{}", set.getLabels());
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			LOGGER.info("识别结果：{}", data[i]);
			result.append(data[i]);
		}
		if(result.length() == 0) {
			return "没有识别到数据";
		}
		return result.toString();
	}

	/**
	 * 添加图片处理：二值化
	 */
	public static final DataSetIterator readProcess(String image) {
		try(RecordReader reader = new ImageRecordReader(28, 28, 1, new ImageTransform() { // 图片颜色二值化
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
//					ImageIO.write(bufferedImage, "jpg", new File("e:/tmp/number/" + UUID.randomUUID().toString() + ".jpg"));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				return new ImageWritable(converter.convert(bufferedImage));
			}
			@Override
			public ImageWritable transform(ImageWritable image) {
				return this.transform(image, new Random());
			}
		})) {
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(ImageTool.base64Content2image(image)));
			bufferedImage = ImageTool.background(bufferedImage); // PNG背景色：原始图片是PNG图片，没有背景色
			File imageFile = new File("tmp.jpg"); // 仅供测试
			ImageIO.write(bufferedImage, "jpg", imageFile);
			reader.initialize(new FileSplit(imageFile));
			DataSetIterator dataSet = new RecordReaderDataSetIterator(reader, 10);
			dataSet.setPreProcessor(new ImagePreProcessingScaler(0, 1)); // 归一化
			return dataSet;
		} catch (IOException | InterruptedException e) {
			LOGGER.error("图片加载异常", e);
		}
		return null;
	}
	
}
