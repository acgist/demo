package com.acgist.regression;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.Before;
import org.junit.Test;

/**
 * 逻辑回归
 */
public class LogisticTest {

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
	public void logistic() {
//		LogisticRegressionWithSGD // 随机梯度下降法
//		LogisticRegressionWithLBFGS // 拟牛顿法梯度下降法
		
		list.add(new LabeledPoint(1D, Vectors.dense(-1D)));
		list.add(new LabeledPoint(0D, Vectors.dense(2D)));
		list.add(new LabeledPoint(1D, Vectors.dense(-3D)));
		list.add(new LabeledPoint(0D, Vectors.dense(4D)));
		list.add(new LabeledPoint(1D, Vectors.dense(-5D)));
		list.add(new LabeledPoint(0D, Vectors.dense(6D)));
		
		JavaRDD<LabeledPoint> rdd = context.parallelize(list, 2);
		
		// 测试和训练
//		JavaRDD<LabeledPoint>[] datas = rdd.randomSplit(new double[] {0.8, 0.2});
//		JavaRDD<LabeledPoint> train = datas[0].cache(); // .cache()实际上是data.persist(StorageLevel.MEMORY_ONLY)，重复使用
//		JavaRDD<LabeledPoint> test = datas[1];
		
		LogisticRegressionWithLBFGS trainer = new LogisticRegressionWithLBFGS().setNumClasses(10);
		LogisticRegressionModel model = trainer.run(rdd.rdd());
		
//		LogisticRegressionModel model = LogisticRegressionWithSGD.train(rdd.rdd(), 100, 1, 1);
//		System.out.println(model.toPMML());
		
		double result = model.predict(Vectors.dense(-10D));
		System.out.println(result);
	}
	
}
