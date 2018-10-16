package com.acgist.bean.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 心情
 */
public class Mood implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final List<Mood> MOODS = new ArrayList<>();

	// 坏心情
	public static final List<String> MOODS_BAD = List.of("伤心", "伤感", "哀伤", "失落", "委屈", "心烦", "忧伤", "恐惧", "悲伤", "悲痛",
			"愤怒", "无助", "气馁", "沮丧", "消极", "烦躁", "生气", "痛苦", "绝望", "迷茫", "郁闷", "酸涩");

	// 好心情
	public static final List<String> MOODS_GOOD = List.of("兴奋", "害羞", "幸福", "幸运", "开心", "得意", "快乐", "恬静", "愉快", "感动",
			"欢乐", "欢喜", "满意", "满足", "激动", "甜蜜", "自信", "陶醉", "骄傲", "高兴");

	private long label; // 标签
	private String value; // 心情

	static {
		AtomicLong labelIndex = new AtomicLong(1000);
		// 坏心情
		MOODS_BAD.forEach(value ->{
			MOODS.add(new Mood(labelIndex.incrementAndGet(), value));
		});
		labelIndex.set(2000);
		// 好心情
		MOODS_GOOD.forEach(value ->{
			MOODS.add(new Mood(labelIndex.incrementAndGet(), value));
		});
	}

	public static void main(String[] args) {
		MOODS.forEach(value -> {
			System.out.println(value);
		});
	}

	public Mood() {
	}

	public Mood(long label, String value) {
		this.label = label;
		this.value = value;
	}

	public long getLabel() {
		return label;
	}

	public void setLabel(long label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.label + "=" + this.value;
	}
	
}
