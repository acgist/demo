package storm.blueprints.spout;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 数据源
 */
public class SentenceSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;

	private int index = 0;
	private SpoutOutputCollector collector;
	
	private String[] sentences = {
		"你好 今天 天气 不错",
		"是的 今天 你 吃过 早餐 了 吗",
		"吃过 了 你 呢",
		"我 也 吃过 了"
	};
	
	/**
	 * 核心方法，通过这个方法发射tuple
	 */
	public void nextTuple() {
		if(this.index >= sentences.length) {
			return;
		}
		String content = sentences[this.index];
		System.out.println("-------------------------开始输入：" + content);
		this.collector.emit(new Values(content));
//		this.collector.emit(new Values(content), "uuid");
		this.index++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Map：storm的配置信息
	 * TopologyContext：提供了topology组件信息
	 * SpoutOutputCollector：提供发射tuple的方法
	 */
	public void open(@SuppressWarnings("rawtypes") Map map, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	/**
	 * 告诉storm组件会发射那些数据流，每个数据流都有那些字段。
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));
	}

	@Override
	public void ack(Object msgId) {
	}
	
}
