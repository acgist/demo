package com.acgist.flink.window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class SocketWindowWordCount {
	
	public static void main(String[] args) throws Exception {
		final int port;
		try {
			final ParameterTool params = ParameterTool.fromArgs(args);
			port = params.getInt("port");
		} catch (Exception e) {
			System.err.println("请添加端口 --port <port>'");
			return;
		}
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<String> text = env.socketTextStream("localhost", port, "\n");
		DataStream<WordWithCount> windowCounts = text.flatMap(new FlatMapFunction<String, WordWithCount>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void flatMap(String value, Collector<WordWithCount> out) {
				for (String word : value.split("\\s")) {
					out.collect(new WordWithCount(word, 1L));
				}
			}
		}).keyBy("word").timeWindow(Time.seconds(5), Time.seconds(1)).reduce(new ReduceFunction<WordWithCount>() {
			private static final long serialVersionUID = 1L;
			@Override
			public WordWithCount reduce(WordWithCount a, WordWithCount b) {
				return new WordWithCount(a.word, a.count + b.count);
			}
		});
		windowCounts.print().setParallelism(1);
		env.execute("Socket Window WordCount");
	}

	public static class WordWithCount {

		public String word;
		public long count;

		public WordWithCount() {
		}

		public WordWithCount(String word, long count) {
			this.word = word;
			this.count = count;
		}

		@Override
		public String toString() {
			return word + " : " + count;
		}
		
	}

}
