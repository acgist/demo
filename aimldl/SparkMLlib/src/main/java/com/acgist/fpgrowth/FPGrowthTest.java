package com.acgist.fpgrowth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.junit.Before;
import org.junit.Test;

public class FPGrowthTest {

	private SparkConf conf;
	private JavaSparkContext context;
	private List<List<String>> list;

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = new ArrayList<>();
	}
	
	@Test
	public void test() {
		list.add(Arrays.asList("r z h k p".split(" ")));
		list.add(Arrays.asList("z y x w v u t s".split(" ")));
		list.add(Arrays.asList("s x o n r".split(" ")));
		list.add(Arrays.asList("x z y m t s q e".split(" ")));
		list.add(Arrays.asList("z".split(" ")));
		list.add(Arrays.asList("x z y r q t p".split(" ")));

		JavaRDD<List<String>> data = context.parallelize(list);
		
		FPGrowth growth = new FPGrowth();
		growth.setMinSupport(0.2) // 最小支持度
			.setNumPartitions(10); // 数据分区
		
		FPGrowthModel<String> model = growth.run(data);
		
		model.generateAssociationRules(0.8) // 最小置信度
//		model.generateAssociationRules(0.4) // 最小置信度
			.toJavaRDD().collect().forEach(value -> {
				System.out.print("可信度：" + value.confidence() + " ");
				System.out.print("结果：" + value.javaConsequent() + " ");
				System.out.println("组合：" + value.javaAntecedent());
			});
		model.freqItemsets().toJavaRDD().foreach(value -> {
			System.out.print("次数：" + value.freq() + " ");
			System.out.println("组合：" + value.javaItems());
		});
	}
	
}
