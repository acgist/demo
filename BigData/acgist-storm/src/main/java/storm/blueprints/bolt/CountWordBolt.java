package storm.blueprints.bolt;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class CountWordBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;

	private OutputCollector collector;
	private Map<String, Integer> counts;
	
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		System.out.println("-------------------------开始统计：" + word + this.counts);
		System.out.println(counts);
		Integer count = counts.get(word);
		if(count == null) {
			count = 0;
		}
		count++;
		counts.put(word, count);
		collector.emit(new Values(word, count));
//		collector.emit(tuple, new Values(word, count)); // 锚定
	}

	public void prepare(@SuppressWarnings("rawtypes") Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		counts = new HashMap<String, Integer>();
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}
	
}
