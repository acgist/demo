package com.acgist.regression;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.junit.Before;
import org.junit.Test;

/**
 * 线性回归
 */
public class LineTest {
	
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
	public void line() {
		// 线性回归伴生对象
//		LinearRegressionWithSGD.train(arg0, arg1)
		// 线性回归类
//		LinearRegressionWithSGD sgd = new LinearRegressionWithSGD(1D, 100, 0D, 1D); // regParam：正则化参数
//		LinearRegressionModel model = sgd.run(input);
		
		list.add(new LabeledPoint(1D, Vectors.dense(1D, 1D)));
		list.add(new LabeledPoint(2D, Vectors.dense(1D, 2D)));
		list.add(new LabeledPoint(3D, Vectors.dense(1D, 3D)));
		list.add(new LabeledPoint(4D, Vectors.dense(1D, 4D)));
		list.add(new LabeledPoint(5D, Vectors.dense(1D, 5D)));
		list.add(new LabeledPoint(6D, Vectors.dense(1D, 6D)));
		
//		list.add(new LabeledPoint(0.4D, Vectors.dense(0.1D, 0.1D)));
//		list.add(new LabeledPoint(0.8D, Vectors.dense(0.2D, 0.2D)));
//		list.add(new LabeledPoint(0.12D, Vectors.dense(0.3D, 0.3D)));
//		list.add(new LabeledPoint(0.16D, Vectors.dense(0.4D, 0.4D)));
		
		JavaRDD<LabeledPoint> rdd = context.parallelize(list, 2);
		LinearRegressionModel model = LinearRegressionWithSGD.train(rdd.rdd(), 100, 1, 1);
		model.weights();
		model.intercept();
		
		System.out.println(model.toPMML());
		
		double result = model.predict(Vectors.dense(1D, 8D));
		System.out.println("结果：" + result);
		
//		model.save(sc, path); // 保存模型
	}
	
}
