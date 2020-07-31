package storm.blueprints.bolt;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/**
 * 不需要发射数据
 */
public class ReportBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;

	private Map<String, Integer> counts;
	
	public void execute(Tuple tuple) {
		System.out.println("-------------------------开始输出");
		String word = tuple.getStringByField("word");
		Integer count = tuple.getIntegerByField("count");
		this.counts.put(word, count);
		System.out.println("当前统计词语：" + word + "，累计出现次数：" + count);
	}

	public void prepare(@SuppressWarnings("rawtypes") Map map, TopologyContext context, OutputCollector collector) {
		counts = new HashMap<String, Integer>();
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		System.out.println("-------------------------报告结果");
		System.out.println(counts);
	}

}
