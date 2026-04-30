package com.acgist;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.operators.MapOperator;

public class SetIterationApplication {

	public static void main(String[] args) throws Exception {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		// 缓存文件
//		env.registerCachedFile(null, null);
		DataSource<Integer> work = env.fromElements(1);
		// 全量迭代
		IterativeDataSet<Integer> loop = work.iterate(100);
		MapOperator<Integer, Integer> solution = loop.map(value -> {
			return value + 1;
		});
		loop.closeWith(solution).print();
//		DataSource<Tuple1<Integer>> work = env.fromElements(Tuple1.of(1));
//		DataSource<Tuple1<Integer>> solution = env.fromElements(Tuple1.of(1));
//		DeltaIteration<Tuple1<Integer>, Tuple1<Integer>> loop = solution
//			// 增量迭代
//			.iterateDelta(work, 100, 0);
//		MapOperator<Tuple1<Integer>, Integer> d = loop.getWorkset().map(value -> Tuple1.of(value));
//		loop.closeWith(d, d).print();
	}
	
}
