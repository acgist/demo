package com.acgist.flink;

import java.io.File;
import java.net.URL;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.sinks.CsvTableSink;
import org.apache.flink.table.sources.CsvTableSource;
import org.apache.flink.table.sources.TableSource;

/**
 * SELECT gk, COUNT(*) AS pv
 * FROM tab 
 * GROUP BY HOP(rowtime, INTERVAL '5' MINUTE, INTERVAL '10' MINUTE), gk 
 */
public class SQLMain {

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1); // 为了打印到控制台的结果不乱序，我们配置全局的并发为1，这里改变并发对结果正确性没有影响
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		TableEnvironment tEnv = TableEnvironment.getTableEnvironment(env);
		URL fileURL = HotMain.class.getClassLoader().getResource("UserBehavior.csv");
		Path filePath = Path.fromLocalFile(new File(fileURL.toURI()));
		TableSource<?> tableSource = CsvTableSource
				.builder()
				.path(filePath.getPath())
				.field("userId", Types.LONG)
				.field("itemId", Types.LONG)
				.field("categoryId", Types.LONG)
				.field("behavior", Types.STRING)
				.field("vtime", Types.LONG)
				.fieldDelimiter(",") // 列分隔符，默认是逗号
				.lineDelimiter("\n") // 行分隔符，回车
//				.ignoreFirstLine() // 忽略第一行
				.ignoreParseErrors() // 忽略解析错误
				.build(); // 构建
		tEnv.registerTableSource("tub", tableSource);
		
		tEnv.registerTableSink("target", new CsvTableSink("e:/target.csv", ",").configure(
				new String[] { "userId", "itemId", "categoryId", "behavior", "vtime" },
				new TypeInformation[] { Types.LONG, Types.LONG, Types.LONG, Types.STRING, Types.LONG }
			));
		tEnv.sqlQuery("select userId, itemId, categoryId, behavior, vtime from tub limit 100").insertInto("target");
		env.execute("Table SQL Query");
	}
	
}
