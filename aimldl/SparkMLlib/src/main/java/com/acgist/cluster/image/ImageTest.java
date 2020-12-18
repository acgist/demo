package com.acgist.cluster.image;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.junit.Before;
import org.junit.Test;

/**
 * 图片分类，特征工程没做好，暂时跳过
 */
public class ImageTest {

	private SparkConf conf;
	private JavaSparkContext context;
	private List<Vector> list;
	
	private Map<String, Vector> images = new TreeMap<>();

	@Before
	public void init() {
		conf = new SparkConf().setAppName("test").setMaster("local");
		context = new JavaSparkContext(conf);
		list = new ArrayList<>();
		String name = null, line;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/vector.txt")))) {
			while((line = reader.readLine()) != null) {
				if(line.length() > 100) {
					String[] strs = line.split(",");
					Double[] array = Stream.of(strs).map(Double::valueOf).toArray(Double[]::new);
					double[] newArray = new double[array.length];
					for (int index = 0; index < array.length; index++) {
						newArray[index] = array[index];
					}
					Vector vector = Vectors.dense(newArray);
					list.add(vector);
					images.put(name, vector);
				} else {
					name = line;
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		} finally {
		}
	}
	
	@Test
	public void kmeans() {
		JavaRDD<Vector> data = context.parallelize(list);
		KMeansModel model = KMeans.train(data.rdd(), 2, 100); // 数据，类型K数量，迭代次数
		images.forEach((key, value) -> {
			int result = model.predict(value);
			System.out.println(key + "=" + result);
		});
	}
	
}
