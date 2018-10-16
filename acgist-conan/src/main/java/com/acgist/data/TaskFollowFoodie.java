package com.acgist.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.acgist.bean.Filter;
import com.acgist.bean.Task;
import com.acgist.bean.TaskFollow;

/**
 * 数据过滤
 */
public class TaskFollowFoodie {

	private static final String REGEX_CHINESE = "[\u4e00-\u9fa5]+"; // 中文
	private static final String REGEX_ENGLISH = "[a-zA-Z]+"; // 英文
	private static final String REGEX_NUMBER = "[0-9]+"; // 数字
	
	/**
	 * 数据过滤
	 */
	public static final void clean(Task task) {
		if(task.getFilters().isEmpty()) {
			return;
		}
		if(task.getFilters().contains(Filter.nofilter)) {
			return;
		}
		List<TaskFollow> pick = new ArrayList<>();
		boolean chinese = task.getFilters().contains(Filter.chinese),
			english = task.getFilters().contains(Filter.english),
			number = task.getFilters().contains(Filter.number),
			length = task.getFilters().contains(Filter.length);
		// 选择
		task.getTaskFollows().forEach(value -> {
			if(chinese && value.getValue().matches(REGEX_CHINESE)) {
				pick.add(value);
			} else if(english && value.getValue().matches(REGEX_ENGLISH)) {
				pick.add(value);
			} else if(number && value.getValue().matches(REGEX_NUMBER)) {
				pick.add(value);
			}
		});
		// 过滤
		List<TaskFollow> list = pick.stream()
			.filter(value -> {
				return length ? value.getValue().length() >= 2 : true;
			})
			.collect(Collectors.toList());
		task.setTaskFollows(list);
	}
	
}
