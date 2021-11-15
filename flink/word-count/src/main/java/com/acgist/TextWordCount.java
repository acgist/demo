package com.acgist;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class TextWordCount {

	public static void main(String[] args) throws Exception {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		DataSource<String> text = env.fromElements("who's there?", "I think I hear them. Stand, ho! Who's there?");
		text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
				for (String word : s.split(" ")) {
					collector.collect(new Tuple2<>(word, 1));
				}
			}
		}).groupBy(0).sum(1).print();
	}

}
