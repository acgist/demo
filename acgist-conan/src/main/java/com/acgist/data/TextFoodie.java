package com.acgist.data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.acgist.bean.TaskFollow;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;

/**
 * 文本处理：分词
 */
public class TextFoodie {

	/**
	 * 词语统计
	 */
	public static final List<TaskFollow> follow(String text) {
		Map<String, Long> countMap = HanLP.segment(text).stream()
		.map(value -> value.word)
		.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		return countMap.entrySet().stream().map(entry -> {
			return new TaskFollow(entry.getKey(), entry.getValue());
		}).collect(Collectors.toList());
	}

	/**
	 * 添加关键字到分词器
	 */
	public static final void keys(List<String> keys) {
		keys.forEach(CustomDictionary::add);
	}

}
