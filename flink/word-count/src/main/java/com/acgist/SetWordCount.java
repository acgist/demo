package com.acgist;

import java.util.stream.Stream;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class SetWordCount {

	public static void main(String[] args) throws Exception {
//		ExecutionEnvironment.createLocalEnvironment(10)
//		StreamExecutionEnvironment.createLocalEnvironment(10)
//		ExecutionEnvironment.createRemoteEnvironment(host, port, args)
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		final DataSource<String> data = env.fromElements("who's there?", "I think I hear them. Stand, ho! Who's there?");
		// DataSet API：不用lambda
//		text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
//				Stream.of(value.split("\\W+")).forEach(word -> out.collect(new Tuple2<>(word, 1)));
//			}
//		})
		// DataSet API：lambda
		data.flatMap((String value, Collector<Tuple2<String, Integer>> out) -> {
			Stream.of(value.split("\\W+")).forEach(word -> out.collect(new Tuple2<>(word, 1)));
		})
		// 指定返回类型
		.returns(Types.TUPLE(Types.STRING, Types.INT))
//		.returns(new TypeHint<Tuple2<String, Integer>>() {})
		// 实现ResultTypeQueryable接口
		// Table API
		.groupBy(0).sum(1).print();
		// 其他写法
//		text.flatMap(new FlatMapFunction<String, Tuple1<String>>() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void flatMap(String value, Collector<Tuple1<String>> out) throws Exception {
//				Stream.of(value.split("\\W+")).forEach(word -> out.collect(new Tuple1<>(word)));
//			}
//		}).map(new MapFunction<Tuple1<String>, Tuple2<String, Integer>>() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public Tuple2<String, Integer> map(Tuple1<String> value) throws Exception {
//				return new Tuple2<String, Integer>(value.getField(0), 1);
//			}
//		}).groupBy(0).sum(1).print();
	}

}
