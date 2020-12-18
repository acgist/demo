package com.acgist.dl4j;

import java.io.File;
import java.io.IOException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

public class CNNFileTest {

	public static void main(String[] args) throws IOException {
		MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(new File("e://number.model"));
//		DataSetIterator testData = new MnistDataSetIterator(2, false, 12345);
//		DataSet set = testData.next();
		DataSetIterator testData = read();
		DataSet set = testData.next();
		System.out.println(set.getFeatureMatrix() + "----");
		int[] data = model.predict(set.getFeatureMatrix());
//		System.out.println(Nd4j.getBlasWrapper().iamax(model.output(set.getFeatureMatrix()).getRow(1)));
//		INDArray data = model.output(set.getFeatures(), false);
		System.out.println(data.length);
//		System.out.println(set.getLabels());
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i] + "-");
		}
	}
	
	public static final DataSetIterator read() {
		try(RecordReader reader = new ImageRecordReader()) {
			reader.initialize(new FileSplit(new File(CNNFileTest.class.getResource("/number/").getPath())));
			DataSetIterator dataSet = new RecordReaderDataSetIterator(reader, 10);
			dataSet.setPreProcessor(new ImagePreProcessingScaler(0, 1));
			return dataSet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
