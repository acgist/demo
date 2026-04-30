package com.acgist;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Expressions;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class TableApplication {

	public static void main(String[] args) {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//		StreamTableEnvironment.create(env);
		StreamTableEnvironment table = StreamTableEnvironment.create(env);
//		table.registerCatalog("user", null);
		table.createTemporaryView(
			"word",
			env.fromElements(new WordCount("a", 1, 1), new WordCount("b", 2, 2))
//			Schema.newBuilder()
//				.column("age", DataTypes.INT())
//				.column("name", DataTypes.STRING())
//				.build()
		);
//		table.toDataStream(null)
		table.from("word")
			.select(Expressions.$("word"), Expressions.$("count"))
//			.window(null)
//			.filter(null)
//			.groupBy(null)
			.where(Expressions.$("count").isEqual(1))
			.execute()
			.print();
//		table.executeSql(null)
		table.sqlQuery("select word from word")
			.execute()
			.print();
	}
	
}
