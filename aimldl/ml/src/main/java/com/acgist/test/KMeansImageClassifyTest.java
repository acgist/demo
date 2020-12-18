package com.acgist.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.clustering.kmeans.KMeansClustering;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

/**
 * 聚类
 */
public class KMeansImageClassifyTest {

	@Test
	public void KMeans() {
		// 设置一个底层生成数值,后面的方法产生的数相等
		// 如Nd4j.randn(5, 5)，产生5行5列的矩阵，不管运行多少次该test用例，均生成相等的5行5列矩阵
		Nd4j.getRandom().setSeed(10);
		// 声明一个KMeans聚类对象，参数分别是 最终聚类的类别数量，迭代次数，距离函数
		KMeansClustering kMeansClustering = KMeansClustering.setup(2, 40, "euclidean");
		// 张量矩阵生成KMeans的点对象
//		List<Point> points = dataA();
		List<Point> points = dataB();
		ClusterSet clusterSet = kMeansClustering.applyTo(points);
		// 将第一个点对象带入进行分类，可得到对象pointClassification，该对象getCenter得到该点所属的类别
		// 可以使用classifyPoint(points.get(0), false)使center中心点不进行更新移动

		clusterSet.getClusters().forEach(value -> {
			StringBuffer content = new StringBuffer();
			content.append("类别：");
			content.append(value.getId() + " ");
			content.append(value.getLabel() + "\n");
			value.getPoints().forEach(point -> {
				content.append(point.getId() + " ");
				content.append(point.getLabel() + " ");
				content.append(point.getArray());
				content.append("\n");
			});
			System.out.println(content);
		});
	}
	
	public List<Point> dataA() {
		List<Point> points = new ArrayList<>();
		points.add(new Point("id-1", "label-1", new double[] {1D, 1D, 1D}));
		points.add(new Point("id-2", "label-2", new double[] {1D, 2D, 2D}));
		points.add(new Point("id-3", "label-3", new double[] {100D, 101D, 200D}));
		points.add(new Point("id-4", "label-4", new double[] {101D, 100D, 201D}));
		return points;
	}
	
	public List<Point> dataB() {
		String name = null, line;
		List<Point> points = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("./vector.txt")))) {
			while((line = reader.readLine()) != null) {
				if(line.length() > 100) {
					String[] strs = line.split(",");
					Double[] array = Stream.of(strs).map(Double::valueOf).toArray(Double[]::new);
					double[] newArray = new double[array.length];
					for (int index = 0; index < array.length; index++) {
						newArray[index] = array[index];
					}
					points.add(new Point("id-" + name, "label-" + name, newArray));
				} else {
					name = line;
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		} finally {
		}
		return points;
	}

}
