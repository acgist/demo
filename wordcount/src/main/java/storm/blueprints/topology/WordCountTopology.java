package storm.blueprints.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import storm.blueprints.bolt.CountWordBolt;
import storm.blueprints.bolt.ReportBolt;
import storm.blueprints.bolt.SplitSentenceBolt;
import storm.blueprints.spout.SentenceSpout;

public class WordCountTopology {

	public static void main(String[] args) throws InterruptedException {
		SentenceSpout spout = new SentenceSpout();

		ReportBolt report = new ReportBolt();
		CountWordBolt count = new CountWordBolt();
		SplitSentenceBolt split = new SplitSentenceBolt();

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", spout);
//		builder.setSpout("spout", spout, 2); // 设置spout的executor总数量，输入量加倍，结果统计加倍

		builder.setBolt("split", split).shuffleGrouping("spout");
//		builder.setBolt("split", split, 2).shuffleGrouping("spout"); // 设置task的executor总数量，不影响结果
//		builder.setBolt("split", split).setNumTasks(2).shuffleGrouping("spout"); // 设置task总数量，不影响结果
//		builder.setBolt("split", split, 2).setNumTasks(4).shuffleGrouping("spout"); // 不影响结果，四个task，两个executor线程，每个线程2个task
		builder.setBolt("count", count).fieldsGrouping("split", new Fields("word"));
		builder.setBolt("report", report).globalGrouping("count");

		Config config = new Config();
		LocalCluster cluster = new LocalCluster();
//		config.setNumWorkers(2); // worker数量，不影响结果

		cluster.submitTopology("countWordTopology", config, builder.createTopology());

		Thread.sleep(10 * 1000);

		cluster.killTopology("countWordTopology");
		cluster.shutdown();
	}

}
