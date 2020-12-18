package com.acgist.classify;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.Before;
import org.junit.Test;

/**
 * 支持向量机
 */
public class SVMTest {
	
	private SparkConf conf;
	private JavaSparkContext context;
	private List<LabeledPoint> list;

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = new ArrayList<>();
	}
	
	@Test
	public void svm() {
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 0D, 0D, 0D)));
		list.add(new LabeledPoint(0D, Vectors.dense(0D, 1D, 0D, 1D)));
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 0D, 0D, 1D)));
		list.add(new LabeledPoint(0D, Vectors.dense(0D, 0D, 0D, 0D)));
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 1D, 0D, 0D)));
		list.add(new LabeledPoint(0D, Vectors.dense(1D, 1D, 0D, 0D)));
		list.add(new LabeledPoint(1D, Vectors.dense(1D, 1D, 0D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(0D, 1D, 1D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(1D, 1D, 1D, 0D)));
		list.add(new LabeledPoint(1D, Vectors.dense(0D, 1D, 1D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(1D, 1D, 1D, 1D)));
		list.add(new LabeledPoint(1D, Vectors.dense(0D, 0D, 1D, 1D)));

		JavaRDD<LabeledPoint> data = context.parallelize(list);
		SVMWithSGD trainer = new SVMWithSGD();
		SVMModel model = trainer.run(data.rdd());
		
		double result = model.predict(Vectors.dense(1D, 0D, 1D, 1D));
		System.out.println(result);
	}

}
