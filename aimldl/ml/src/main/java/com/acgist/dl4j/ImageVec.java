package com.acgist.dl4j;

import java.io.File;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class ImageVec {

	public static void main(String[] args) {
		try(RecordReader reader = new ImageRecordReader(750, 500, 1)) {
			reader.initialize(new FileSplit(new File(ImageVec.class.getResource("/image/").getPath())));
			System.out.println(reader.next());
			DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(reader, 10);
			System.out.println(dataSetIterator.next());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
