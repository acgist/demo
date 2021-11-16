package com.acgist;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class StreamOrderApplication {

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		final DataStream<String> stream = env.socketTextStream("localhost", 9000, "\n");
		final SingleOutputStreamOperator<Tuple6<Long, String, Double, String, Integer, Double>> orderStream = stream.map(content -> {
			// 时间-区域-金额-名称-数量-总额
			final String[] values = content.split("-");
			final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			return Tuple6.of(
				format.parse(values[0]).getTime(),
				values[1],
				Double.valueOf(values[2]),
				values[3],
				Integer.valueOf(values[4]),
				Double.valueOf(values[2]) * Integer.valueOf(values[4])
			);
		})
		.returns(Types.TUPLE(Types.LONG, Types.STRING, Types.DOUBLE, Types.STRING, Types.INT, Types.DOUBLE))
		.assignTimestampsAndWatermarks(
			WatermarkStrategy.<Tuple6<Long, String, Double, String, Integer, Double>>forBoundedOutOfOrderness(Duration.ofSeconds(5))
			.withTimestampAssigner((value, timestamp) -> value.f0)
		)
		.filter(value -> value.f5 > 0D);
//		// 交易总额
//		orderStream
//			.map(value -> value.f5)
//			.windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
//			.sum(0)
//			.print("交易总额");
//		// 热点区域
//		orderStream
//			.map(value -> Tuple2.of(value.f1, value.f5))
//			.returns(Types.TUPLE(Types.STRING, Types.DOUBLE))
//			.keyBy(value -> value.f0)
//			.window(TumblingEventTimeWindows.of(Time.seconds(5)))
//			.reduce((source, target) -> Tuple2.of(source.f0, source.f1 + target.f1))
//			.print("热点区域");
		// 热点商品
		orderStream
			.map(value -> Tuple2.of(value.f3, value.f5))
			.returns(Types.TUPLE(Types.STRING, Types.DOUBLE))
			.keyBy(value -> value.f0)
			.window(TumblingEventTimeWindows.of(Time.seconds(5)))
//			.reduce((source, target) -> Tuple2.of(source.f0, source.f1 + target.f1))
//			.reduce(
//				(source, target) -> Tuple2.of(source.f0, source.f1 + target.f1),
//				new WindowFunction<Tuple2<String, Double>, Tuple2<String, Double>, String, TimeWindow>() {
//					private static final long serialVersionUID = 1L;
//					private long last = 0L;
//					private final List<Tuple2<String, Double>> list = new ArrayList<>();
//					@Override
//					public void apply(String key, TimeWindow window, Iterable<Tuple2<String, Double>> input, Collector<Tuple2<String, Double>> out) throws Exception {
//						if(this.last != window.getEnd()) {
//							this.list.sort((source, target) -> source.f1.compareTo(target.f1));
//							this.list.forEach(out::collect);
//							this.list.clear();
//							this.last = window.getEnd();
//						}
//						input.forEach(this.list::add);
//					}
//				}
//			)
			.reduce(
				(source, target) -> Tuple2.of(source.f0, source.f1 + target.f1),
				(String key, TimeWindow window, Iterable<Tuple2<String, Double>> input, Collector<Tuple3<String, Double, Long>> out) -> {
					input.forEach(value -> out.collect(Tuple3.of(value.f0, value.f1, window.getEnd())));
				}
			)
//			.map(value -> Tuple3.of(t, null, null))
			.returns(Types.TUPLE(Types.STRING, Types.DOUBLE, Types.LONG))
			.keyBy(value -> value.f2)
			.process(new KeyedProcessFunction<Long, Tuple3<String, Double, Long>, Tuple3<String, Double, Long>>() {
				private static final long serialVersionUID = 1L;
				// 状态
				private transient ListState<Tuple3<String, Double, Long>> list = null;
				public void open(org.apache.flink.configuration.Configuration parameters) throws Exception {
					super.open(parameters);
		            final ListStateDescriptor<Tuple3<String, Double, Long>> list = new ListStateDescriptor<>("list", Types.TUPLE(Types.STRING, Types.DOUBLE, Types.LONG));
		            this.list = getRuntimeContext().getListState(list);
				};
				@Override
				public void processElement(Tuple3<String, Double, Long> input, KeyedProcessFunction<Long, Tuple3<String, Double, Long>, Tuple3<String, Double, Long>>.Context context, Collector<Tuple3<String, Double, Long>> out) throws Exception {
					this.list.add(input);
					context.timerService().registerEventTimeTimer(input.f2 + 1);
				}
				@Override
				public void onTimer(long timestamp, org.apache.flink.streaming.api.functions.KeyedProcessFunction<Long,org.apache.flink.api.java.tuple.Tuple3<String,Double,Long>,org.apache.flink.api.java.tuple.Tuple3<String,Double,Long>>.OnTimerContext ctx, org.apache.flink.util.Collector<org.apache.flink.api.java.tuple.Tuple3<String,Double,Long>> out) throws Exception {
					final List<Tuple3<String, Double, Long>> list = new ArrayList<>();
					this.list.get().forEach(value -> list.add(value));
					this.list.clear();
					list.sort((source, target) -> target.f1.compareTo(source.f1));
					// TOP3
//					list.subList(0, 3).forEach(out::collect);
					list.forEach(out::collect);
				}
			})
			.print("热点商品");
		orderStream
			.keyBy(value -> value.f1)
			.keyBy(value -> value.f3)
			.window(TumblingEventTimeWindows.of(Time.seconds(5)))
			// 时间-区域-金额-名称-数量-总额
			.reduce((source, target) -> Tuple6.of(source.f0, source.f1, source.f2, source.f3, source.f4 + target.f4, source.f5 + target.f5))
			.print("区域商品");
		env.execute("Flink Order");
	}
}
