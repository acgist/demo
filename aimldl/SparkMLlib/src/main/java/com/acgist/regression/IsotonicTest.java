package com.acgist.regression;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.IsotonicRegression;
import org.apache.spark.mllib.regression.IsotonicRegressionModel;
import org.junit.Before;
import org.junit.Test;

import scala.Tuple3;

/**
 * 保序回归算法
 */
public class IsotonicTest {

	private SparkConf conf;
	private JavaSparkContext context;
	private List<Tuple3<Double, Double, Double>> list;

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = new ArrayList<>();
	}
	
	@Test
	public void isotonic() {
		for (int i = 1; i < 100; i++) {
			list.add(Tuple3.<Double, Double, Double>apply(i + 0D, i + new Random().nextDouble(), 1D));
		}
		
		JavaRDD<Tuple3<Double, Double, Double>> rdd = context.parallelize(list, 2);
		
		IsotonicRegression trainer = new IsotonicRegression();
		IsotonicRegressionModel model = trainer.run(rdd);
		
		double result = model.predict(43D);
		System.out.println(result);
	}
	
}
