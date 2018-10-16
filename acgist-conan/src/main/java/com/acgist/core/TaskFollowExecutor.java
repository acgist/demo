package com.acgist.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.bean.Task;
import com.acgist.bean.Task.Match;
import com.acgist.bean.TaskFollow;
import com.acgist.data.TextFoodie;
import com.acgist.html.HTMLFoodie;

/**
 * 页面子任务执行器
 */
public class TaskFollowExecutor {

	private Task task;
	private String link;

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskFollowExecutor.class);
	
	public TaskFollowExecutor(Task task, String link) {
		this.task = task;
		this.link = link;
	}

	public List<TaskFollow> execute() {
		LOGGER.info("{}-开始抓取内容页面，页面链接：{}", this.task.getName(), this.link);
		HTMLFoodie foodie = HTMLFoodie.html(this.link);
		String text = foodie.text();
		boolean match = false;
		if(task.getMatch() == Match.all) { // 全匹配
			match = task.getKeys().stream().allMatch(key -> text.contains(key));
		} else if(task.getMatch() == Match.part) { // 部分匹配
			match = task.getKeys().stream().anyMatch(key -> text.contains(key));
		}
		if(match) {
			return TextFoodie.follow(text);
		}
		return new ArrayList<>();
	}
	
}
