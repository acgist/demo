package com.acgist;

import java.time.Duration;
import java.util.stream.Stream;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class StreamWordCount {

	public static void main(String[] args) throws Exception {
//		final ParameterTool params = ParameterTool.fromArgs(args);
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
		// 周期生成watermark：periodic
		// 数量生成watermark：punctuated
//		env.getConfig().setAutoWatermarkInterval(100);
//		env.getConfig().enableForceAvro();
//		env.getConfig().enableForceKryo();
//		env.getConfig().addDefaultKryoSerializer(null, null);
		// 重写source指定watermark
		final DataStream<String> data = env.socketTextStream("localhost", 9000, "\n");
		// 合并
//		data.union(null);
//		泛型注解：@TypeInfo
		data.flatMap((String value, Collector<WordCount> out) -> {
			Stream.of(value.split("\\W+")).forEach(word -> out.collect(new WordCount(word, 1, System.currentTimeMillis())));
		})
		.returns(WordCount.class)
		// 单调递增：WatermarkStrategy.<WordCount>forBoundedOutOfOrderness(Duration.ofSeconds(0))
//		.assignTimestampsAndWatermarks(WatermarkStrategy.<WordCount>forMonotonousTimestamps())
		// 固定延迟
		.assignTimestampsAndWatermarks(
			WatermarkStrategy.<WordCount>forBoundedOutOfOrderness(Duration.ofSeconds(2))
			// 设置时间：IngestionTimeAssigner、RecordTimestampAssigner
//			.withTimestampAssigner((wordCount, timestamp) -> wordCount.getTime())
			// 空闲时间
//			.withIdleness(Duration.ofMillis(1))
		)
		// 不推荐使用
//		.keyBy(0)
//		.keyBy("word")
//		DataSet.groupBy - DataStream.keyBy
//		不能使用keyBy：没有重写hashCode、任何数组
		.keyBy(word -> word.getWord())
		// Non-Keyed
//		.windowAll(null)
		// 基于数量
//		.countWindow(5)
		// 基于时间：滚动窗口（Tumbling）、滑动窗口（Sliding）、会话窗口（Session）、全局窗口（Global）
		// 不推荐使用
//		.timeWindow(Time.seconds(5))
		// 全局窗口
//		.window(GlobalWindows.create())
		// 注意时间类型：事件时间、接入时间、处理时间
		.window(TumblingEventTimeWindows.of(Time.seconds(5)))
//		.window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
		// 指定时间偏移
//		.window(TumblingEventTimeWindows.of(Time.seconds(5), Time.seconds(2)))
		// 触发事件：全局窗口
//		.trigger(null)
		// 迟到数据
//		.allowedLateness(null)
		// 输出Tag：可以用来分流延迟和正确数据
//		.sideOutputLateData(null)
		// 窗口函数：增量聚合：reduce、aggregate、fold（已经删除）
		// 基于中间状态计算，占用内存少。
		.reduce((source, target) -> new WordCount(source.getWord(), source.getCount() + target.getCount(), 0L))
		// 窗口函数：全量函数：process
		// 性能较弱，用来数据缓存。
//		.process(new ProcessWindowFunction<WordCount, Integer, String, TimeWindow>() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public void process(String name, Context context, Iterable<WordCount> iterable, Collector<Integer> out) throws Exception {
//			}
//		})
		// 两种窗口结合
//		.reduce(null, null)
		// 配合sideOutputLateData
//		.getSideOutput(null)
		.print()
//		.writeAsText(path)
		.setParallelism(1);
		// 流计算需要显示调用
		env.execute("StreamWordCount");
	}
	
}
